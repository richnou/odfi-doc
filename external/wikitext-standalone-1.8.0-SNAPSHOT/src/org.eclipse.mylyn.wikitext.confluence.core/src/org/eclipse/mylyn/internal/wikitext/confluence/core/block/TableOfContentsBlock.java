/*******************************************************************************
 * Copyright (c) 2007, 2008 David Green and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Green - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.internal.wikitext.confluence.core.block;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.mylyn.wikitext.confluence.core.ConfluenceLanguage;
import org.eclipse.mylyn.wikitext.core.parser.Attributes;
import org.eclipse.mylyn.wikitext.core.parser.DocumentBuilder.BlockType;
import org.eclipse.mylyn.wikitext.core.parser.outline.OutlineItem;
import org.eclipse.mylyn.wikitext.core.parser.outline.OutlineParser;

/**
 * @author David Green
 */
public class TableOfContentsBlock extends ParameterizedBlock {

	static final Pattern startPattern = Pattern.compile("\\s*\\{toc(?::([^\\}]+))?\\}\\s*(.+)?"); //$NON-NLS-1$

	private int blockLineNumber = 0;

	private String style = "none"; //$NON-NLS-1$

	private int maxLevel = Integer.MAX_VALUE;

	private Matcher matcher;

	@Override
	public int processLineContent(String line, int offset) {
		if (blockLineNumber++ > 0) {
			setClosed(true);
			return 0;
		}

		if (!getMarkupLanguage().isFilterGenerativeContents()) {
			String options = matcher.group(1);
			setOptions(options);

			OutlineParser outlineParser = new OutlineParser(new ConfluenceLanguage());
			OutlineItem rootItem = outlineParser.parse(state.getMarkupContent());
			emitToc(rootItem);
		}
		setClosed(true);
		return matcher.start(2);
	}

	private void emitToc(OutlineItem item) {
		if (item.getChildren().isEmpty()) {
			return;
		}
		if ((item.getLevel() + 1) > maxLevel) {
			return;
		}
		Attributes nullAttributes = new Attributes();

		builder.beginBlock(BlockType.NUMERIC_LIST, new Attributes(null, null, "list-style: " + style + ";", null)); //$NON-NLS-1$ //$NON-NLS-2$
		for (OutlineItem child : item.getChildren()) {
			builder.beginBlock(BlockType.LIST_ITEM, nullAttributes);
			builder.link('#' + child.getId(), child.getLabel());
			emitToc(child);
			builder.endBlock();
		}
		builder.endBlock();
	}

	@Override
	public boolean canStart(String line, int lineOffset) {
		style = "none"; //$NON-NLS-1$
		maxLevel = Integer.MAX_VALUE;
		blockLineNumber = 0;

		matcher = startPattern.matcher(line);
		if (lineOffset > 0) {
			matcher.region(lineOffset, line.length());
		}
		if (matcher.matches()) {
			return true;
		} else {
			matcher = null;
			return false;
		}
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	@Override
	protected void setOption(String key, String value) {
		if (key.equals("style")) { //$NON-NLS-1$
			setStyle(value);
		} else if (key.equals("maxLevel")) { //$NON-NLS-1$
			try {
				maxLevel = Integer.parseInt(value);
			} catch (NumberFormatException e) {
			}
		}
	}

}
