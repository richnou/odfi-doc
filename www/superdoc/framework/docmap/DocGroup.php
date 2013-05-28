<?php


class DocGroup {

	/**
	 * Name of the group
	 * @var unknown
	 */
	public $name = "";

	/**
	 *  ID used to uniquely identify group using simple string
	 */
	public $id = "";

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

     Ignore matching paths

	*/
	public $ignores = [];

	/**
	 * Default constructor of DocGroup needs a name for the group
	 * @param string $name
	 */
	function __construct($name) {

		$this->name = $name;
		$this->id = $name;

	}

	function addIgnore($ignore) {

		$this->ignores[] = $ignore;

	}

	function pathIsIgnored($path) {

		foreach ($this->ignores as $key => $ignore ) {

			if ( fnmatch($ignore,$path)) {
				return true;
			}

		}

		return false;

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

			//-- Ignores ?
			if ($this->pathIsIgnored($file))
				continue;

			//-- Add ?
			if ($file!=".." && $file != "." && $file != ".git" && is_dir($this->docSource.'/'.$file)) {
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


		// Search Markdown
		//------------------------
		$docs = glob($this->docSource."/*.md");

		//-- Convert to Document objects
		$res = array();
		foreach ($docs as $doc) {
			$res[] = new Document((string)$doc);
		}


		// Search for index.html that will be seen as Jump Link
		//---------------
		$index = $this->docSource."/index.html";
		if (is_file($index)) {

			$doc = new Document((string)$index);
			$doc->link = true;
			$res[] = $doc;


		}

		return $res;

	}

}

?>
