
import sys
import os
from os.path import basename
from os.path import dirname

from docutils import nodes
from docutils.parsers.rst import Directive
from sphinx.util.compat import make_admonition
from sphinx.locale import _


from sphinx.util import logging
import logging
import re


from xml.etree import ElementTree


class StepsRecorder(nodes.Inline, nodes.Element):
	"""StepsRecorder Node"""



def visit_StepsRecorder_node(self, node):

	## Extract XML
	mainHTML = node.resultParts["main.htm"]
	#print("Search HTML: "+mainHTML)
	reportXML = re.search("<Report>.*</Report>",mainHTML)

	#print("Found Report: "+reportXML.group(0))

	report = ElementTree.fromstring(reportXML.group(0))

	## Create For each step a Div
	self.body.append('<div class="steps">')
	self.body.append('<div class="steps-header">Steps below:</div>')
	eachAction = report.findall("./UserActionData/RecordSession/EachAction")
	for action in eachAction: 
		number =  action.get("ActionNumber")
		print("Action: "+number)
		self.body.append('<div class="step">')


		if action.get("ActionNumber") in node.resultParts:
			self.body.append('<div class="caption"> %s </div>' %  node.resultParts[action.get("ActionNumber")])

		## Add Picture
		self.body.append('<div class="picture">')
		self.body.append('<img src="data:image/jpeg;base64,%s"/>' % node.resultParts[action.findtext("ScreenshotFileName")])
		self.body.append('</div>')

		# EOF Step
		self.body.append('</div>')
		
	
	self.body.append('</div>')

	pass
	

def depart_StepsRecorder_node(self, node):
	pass

class OdfiStepsRecorderDirective(Directive):

  
	has_content = True
	required_arguments = 1 
	optional_arguments = 64
           

	def run(self):

		print('Running ODFI Steps Recorder Directive')
		print( self.arguments)
		print( self.options)
		print( self.content)
		
		env = self.state.document.settings.env
		source = self.state_machine.input_lines.source(self.lineno - self.state_machine.input_offset - 1)

		## Get File, relative to current source
		fileName = self.arguments[0]
		sourceDirectory = dirname(source)
		stepsFile  = os.path.normpath(sourceDirectory+"/"+fileName)
		if os.path.exists(stepsFile):
			print("Steps File is at: ",stepsFile)
		else:
			return [nodes.error(None, nodes.paragraph(text = "Unable to Load Steps File at %s:%d: file %s does not exist" % (basename(source), self.lineno,stepsFile))) ]


		## Read file and split parts
		#################
		stepsNode = StepsRecorder()

		stepsBlock = nodes.container();

		with open(stepsFile) as f: stepsContent = f.read()

		## get boundary
		match = re.search('boundary="([\w_=\+\.\-]+)"',stepsContent)
		boundary= "--"+match.group(1)
		print("Steps Boundary: "+boundary)

		## Split
		parts = stepsContent.split(boundary)
		print("Parts: %s" % len(parts))

		## Store Each Part with name
		resultParts = {}
		for partContentIndex in range(1, len(parts)):

			## Get Part content line by line, to extract parameters
			## iF an empty line is encountered, it is the beginning of the content
			partContent = parts[partContentIndex].strip()
			partFileContent = ""
			fileName = ""
			contentStart = False
			lines = partContent.split("\n")
			for lineIndex in range(0, len(lines)):
				
				line = lines[lineIndex]
				
				if contentStart is True: 
					partFileContent+=line
				elif line.startswith("Content-Location:"):
					fileName =  line.strip("Content-Location:").strip()
					print("Part File Name: "+fileName)
				elif line == "":
					print("Found Content Start")
					contentStart = True 

			# Save part file content
			resultParts[fileName] = partFileContent

			## Make admonition

		## Save result Parts to node 
		stepsNode.resultParts = resultParts

		## Read Directive Content and save Step Description
		for contentLine in self.content: 
			match = re.search("([\d]+)\s+(.*)",contentLine)
			number = match.group(1)
			desc = match.group(2)
			resultParts[number] = desc

		return [stepsNode]

def setup(app):

	"""html=(visit_OdfiSVG_node, depart_OdfiSVG_node)"""
	app.add_node(StepsRecorder, html=(visit_StepsRecorder_node, depart_StepsRecorder_node))
	app.add_directive('odfi.stepsrecorder',OdfiStepsRecorderDirective)
	return {'version': '1.0.0'}