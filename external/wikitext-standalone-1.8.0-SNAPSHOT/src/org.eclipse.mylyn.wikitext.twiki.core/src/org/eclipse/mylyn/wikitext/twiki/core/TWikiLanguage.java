/*******************************************************************************
 * Copyright (c) 2007, 2009 David Green and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Green - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.wikitext.twiki.core;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.mylyn.internal.wikitext.twiki.core.block.DefinitionListBlock;
import org.eclipse.mylyn.internal.wikitext.twiki.core.block.HeadingBlock;
import org.eclipse.mylyn.internal.wikitext.twiki.core.block.HorizontalRuleBlock;
import org.eclipse.mylyn.internal.wikitext.twiki.core.block.ListBlock;
import org.eclipse.mylyn.internal.wikitext.twiki.core.block.LiteralBlock;
import org.eclipse.mylyn.internal.wikitext.twiki.core.block.ParagraphBlock;
import org.eclipse.mylyn.internal.wikitext.twiki.core.block.TableOfContentsBlock;
import org.eclipse.mylyn.internal.wikitext.twiki.core.block.VerbatimBlock;
import org.eclipse.mylyn.internal.wikitext.twiki.core.phrase.AutoLinkSwitchPhraseModifier;
import org.eclipse.mylyn.internal.wikitext.twiki.core.phrase.SimplePhraseModifier;
import org.eclipse.mylyn.internal.wikitext.twiki.core.token.IconReplacementToken;
import org.eclipse.mylyn.internal.wikitext.twiki.core.token.ImpliedEmailLinkReplacementToken;
import org.eclipse.mylyn.internal.wikitext.twiki.core.token.LinkReplacementToken;
import org.eclipse.mylyn.internal.wikitext.twiki.core.token.WikiWordReplacementToken;
import org.eclipse.mylyn.wikitext.core.parser.DocumentBuilder.SpanType;
import org.eclipse.mylyn.wikitext.core.parser.markup.AbstractMarkupLanguage;
import org.eclipse.mylyn.wikitext.core.parser.markup.Block;
import org.eclipse.mylyn.wikitext.core.parser.markup.phrase.HtmlEndTagPhraseModifier;
import org.eclipse.mylyn.wikitext.core.parser.markup.phrase.HtmlStartTagPhraseModifier;
import org.eclipse.mylyn.wikitext.core.parser.markup.token.EntityReferenceReplacementToken;
import org.eclipse.mylyn.wikitext.core.parser.markup.token.ImpliedHyperlinkReplacementToken;

// TODO: table
// TODO: empty line to empty para
// TODO: images?  is it supported by TWiki?
// TODO: variables, see http://twiki.org/cgi-bin/view/TWiki04x02/TWikiVariables
// TODO: implement <sticky>
// TODO: named anchors eg: #Target  from the docs: To define an anchor write #AnchorName at the beginning of a line. The anchor name must be a WikiWord of no more than 32 characters.

/**
 * a markup language implementing TWiki syntax. See <a
 * href="http://wikix.ilog.fr/wiki/bin/view/TWiki/TextFormattingRules">TWiki Formatting Rules</a> for details.
 * 
 * @author David Green
 * @since 1.0
 */
public class TWikiLanguage extends AbstractMarkupLanguage {

	private final PatternBasedSyntax literalTokenSyntax = new PatternBasedSyntax();

	private final PatternBasedSyntax literalPhraseModifierSyntax = new PatternBasedSyntax();

	private boolean literalMode;

	private boolean isAutoLinking = true;

	private String iconPattern = "TWikiDocGraphics/{0}.gif"; // FIXME find out if this is correct //$NON-NLS-1$

	public TWikiLanguage() {
		setName("TWiki"); //$NON-NLS-1$
		setInternalLinkPattern("/cgi-bin/view/{0}/{1}"); //$NON-NLS-1$
	}

	@Override
	protected PatternBasedSyntax getPhraseModifierSyntax() {
		return literalMode ? literalPhraseModifierSyntax : phraseModifierSyntax;
	}

	@Override
	protected PatternBasedSyntax getReplacementTokenSyntax() {
		return literalMode ? literalTokenSyntax : tokenSyntax;
	}

	@Override
	protected void clearLanguageSyntax() {
		super.clearLanguageSyntax();
		literalTokenSyntax.clear();
		literalPhraseModifierSyntax.clear();
	}

	private Block paragraphBreakingBlock(Block block) {
		paragraphBreakingBlocks.add(block);
		return block;
	}

	/**
	 * for the purpose of converting wiki words into links, determine if the wiki word exists.
	 * 
	 * @see WikiWordReplacementToken
	 */
	public boolean computeInternalLinkExists(String link) {
		return true;
	}

	/**
	 * Convert a page name to an href to the page.
	 * 
	 * @param pageName
	 *            the name of the page to target, usually a WikiWord with whitespace removed
	 * @return the href to access the page
	 * @see #getInternalPageHrefPrefix()
	 */
	public String toInternalHref(String pageName) {
		String[] parts = pageName.split("\\."); //$NON-NLS-1$
		if (parts.length == 1) {
			parts = new String[] { "Main", parts[0] }; //$NON-NLS-1$
		}
		return MessageFormat.format(super.internalLinkPattern, (Object[]) parts);
	}

	public String toIconUrl(String iconType) {
		return MessageFormat.format(getIconPattern(), iconType);
	}

	/**
	 * indicate if we're currently processing a literal block
	 * 
	 * @see LiteralBlock
	 */
	public boolean isLiteralMode() {
		return literalMode;
	}

	/**
	 * indicate if we're currently processing a literal block
	 * 
	 * @see LiteralBlock
	 */
	public void setLiteralMode(boolean literalMode) {
		this.literalMode = literalMode;
	}

	/**
	 * the pattern to use when generating icon image urls.
	 */
	public void setIconPattern(String iconPattern) {
		this.iconPattern = iconPattern;
	}

	/**
	 * the pattern to use when generating icon image urls.
	 */
	public String getIconPattern() {
		return iconPattern;
	}

	public boolean isAutoLinking() {
		return isAutoLinking;
	}

	public void setAutoLinking(boolean isAutoLinking) {
		this.isAutoLinking = isAutoLinking;
	}

	@Override
	protected void addStandardBlocks(List<Block> blocks, List<Block> paragraphBreakingBlocks) {
		// IMPORTANT NOTE: Most items below have order dependencies.  DO NOT REORDER ITEMS BELOW!!

		blocks.add(paragraphBreakingBlock(new VerbatimBlock()));
		blocks.add(paragraphBreakingBlock(new LiteralBlock()));
		blocks.add(paragraphBreakingBlock(new HorizontalRuleBlock()));
		blocks.add(paragraphBreakingBlock(new HeadingBlock()));
		blocks.add(paragraphBreakingBlock(new DefinitionListBlock()));
		blocks.add(paragraphBreakingBlock(new TableOfContentsBlock()));
		blocks.add(paragraphBreakingBlock(new ListBlock()));
	}

	@Override
	protected void addStandardPhraseModifiers(PatternBasedSyntax phraseModifierSyntax) {
		// IMPORTANT NOTE: Most items below have order dependencies.  DO NOT REORDER ITEMS BELOW!!

		boolean escapingHtml = configuration == null ? false : configuration.isEscapingHtmlAndXml();

		phraseModifierSyntax.add(new AutoLinkSwitchPhraseModifier());

		if (!escapingHtml) {
			phraseModifierSyntax.add(new HtmlStartTagPhraseModifier());
			phraseModifierSyntax.add(new HtmlEndTagPhraseModifier());
		}
		phraseModifierSyntax.beginGroup("(?:(?<=[\\s\\.,\\\"'?!;:\\)\\(\\{\\}\\[\\]])|^)(?:", 0); //$NON-NLS-1$
		phraseModifierSyntax.add(new SimplePhraseModifier("*", SpanType.BOLD)); //$NON-NLS-1$
		phraseModifierSyntax.add(new SimplePhraseModifier("__", new SpanType[] { SpanType.BOLD, SpanType.ITALIC })); //$NON-NLS-1$
		phraseModifierSyntax.add(new SimplePhraseModifier("_", SpanType.ITALIC)); //$NON-NLS-1$
		phraseModifierSyntax.add(new SimplePhraseModifier("==", new SpanType[] { SpanType.BOLD, SpanType.MONOSPACE })); //$NON-NLS-1$
		phraseModifierSyntax.add(new SimplePhraseModifier("=", SpanType.MONOSPACE)); //$NON-NLS-1$
		phraseModifierSyntax.endGroup(")(?=\\W|$)", 0); //$NON-NLS-1$

		literalPhraseModifierSyntax.add(new HtmlStartTagPhraseModifier());
		literalPhraseModifierSyntax.add(new HtmlEndTagPhraseModifier());
	}

	@Override
	protected void addStandardTokens(PatternBasedSyntax tokenSyntax) {
		// IMPORTANT NOTE: Most items below have order dependencies.  DO NOT REORDER ITEMS BELOW!!

		tokenSyntax.add(new EntityReferenceReplacementToken("(tm)", "#8482")); //$NON-NLS-1$ //$NON-NLS-2$
		tokenSyntax.add(new EntityReferenceReplacementToken("(TM)", "#8482")); //$NON-NLS-1$ //$NON-NLS-2$
		tokenSyntax.add(new EntityReferenceReplacementToken("(c)", "#169")); //$NON-NLS-1$ //$NON-NLS-2$
		tokenSyntax.add(new EntityReferenceReplacementToken("(C)", "#169")); //$NON-NLS-1$ //$NON-NLS-2$
		tokenSyntax.add(new EntityReferenceReplacementToken("(r)", "#174")); //$NON-NLS-1$ //$NON-NLS-2$
		tokenSyntax.add(new EntityReferenceReplacementToken("(R)", "#174")); //$NON-NLS-1$ //$NON-NLS-2$
		tokenSyntax.add(new LinkReplacementToken());
		tokenSyntax.add(new ImpliedHyperlinkReplacementToken());
		tokenSyntax.add(new ImpliedEmailLinkReplacementToken());
		tokenSyntax.add(new WikiWordReplacementToken());
		tokenSyntax.add(new IconReplacementToken());

		literalTokenSyntax.add(new ImpliedHyperlinkReplacementToken());
	}

	@Override
	protected Block createParagraphBlock() {
		return new ParagraphBlock();
	}

}
