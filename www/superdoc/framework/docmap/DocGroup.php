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
	 * Default constructor of DocGroup needs a name for the group
	 * @param string $name
	 */
	function __construct($name) {
		
		$this->name = $name;
		
	}
	
	
}

?>