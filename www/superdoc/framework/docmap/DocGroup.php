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
	public $docSource = [];

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

		$res = array();
		foreach ($this->docSource as $ds) {

			//-- Scan
			$files  = scandir($ds);

			//-- Filter Folders as Groups
			foreach($files as $file) {

				//echo "File: $file";

				//-- Ignores ?
				if ($this->pathIsIgnored($file))
					continue;

				//-- Add ?
				if ($file!=".." && $file != "." && $file != ".git" && is_dir($ds.'/'.$file)) {
					$group = new DocGroup((string)basename($file));
					$group->ignores = $this->ignores;
					$group->docSource[] = $ds.'/'.$file;
					$res[] = $group;
				}
			}

			//print_r($res);



		}

		return $res;


	}

	/**
	 * Returns paths to all markdown documents in docSource
	 *
	 * @return a list of Document
	 */
	function getDocuments() {

		foreach ($this->docSource as $ds) {

			// Search Markdown
			//------------------------
			$docs = glob($ds."/*.md");

			//-- Convert to Document objects
			$res = array();
			foreach ($docs as $doc) {
				$res[] = new Document((string)$doc);
			}


			// Search for index.html that will be seen as Jump Link
			//---------------
			$index = $ds."/index.html";
			if (is_file($index)) {

				$doc = new Document((string)$index);
				$doc->link = true;
				$res[] = $doc;


			}


		}



		return $res;

	}

}

?>
