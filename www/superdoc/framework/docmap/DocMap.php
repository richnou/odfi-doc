<?php 
class DocMap {
	
	
	protected $groups = array();
	
	
	
	/**
	 * Creates a new named group
	 * @param string $name
	 * @return The created object
	 */
	public function newGroup($name) {
		
		$group = new DocGroup($name);
		$this->addGroup($group);
		return $group;
		
	}
	
	public function addGroup(DocGroup $group) {
		
		$this->groups[] = $group;
		
	}
	
}


?>