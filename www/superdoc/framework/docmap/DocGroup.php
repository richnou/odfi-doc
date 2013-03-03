<?php 


class DocGroup {
	
	/**
	 * Name of the group
	 * @var unknown
	 */
	public $name = "";
	
	/**
	 * Path of a folder where to find documentations
	 * @var unknown
	 */
	public $docSource = null;
	
	/**
	 * If true, the group will only be displayd on specific request
	 * @var unknown
	 */
	public $hidden = false;
	
	/**
	 * Default constructor of DocGroup needs a name for the group
	 * @param string $name
	 */
	function __construct($name) {
		
		$this->name = $name;
		
	}
	
	/**
	 * Returns subgroups of this group:
	 *  - The folders in docsource
	 */
	function getGroups() {
		
		//-- Scan
		$files  = scandir($this->docSource);
		
		//-- Filter Folders as Groups
		$res = array();
		foreach($files as $file) {
			
			//echo "File: $file";
			
			if ($file!=".." && $file != "." && is_dir($this->docSource.'/'.$file)) {
				$group = new DocGroup((string)basename($file));
				$group->docSource = $this->docSource.'/'.$file;
				$res[] = $group;
			}
		}
		
		//print_r($res);
		
		return $res;
		
	}
	
	/**
	 * Returns paths to all markdown documents in docSource
	 * 
	 * @return a list of Document
	 */
	function getDocuments() {
		
		
		//-- Search
		$docs = glob($this->docSource."/*.md");
		
		//-- Convert to Document objects
		$res = array();
		foreach ($docs as $doc) {
			$res[] = new Document((string)$doc);
		}
		return $res;
		
	}
	
}

?>