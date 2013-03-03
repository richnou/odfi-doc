<?php 
class DocMap {
	
	
	protected $groups = array();
	
	
	public function getGroup($name) {
		//echo "Returning:  ".$this->groups[$name];
		return $this->groups[$name];
	}
	
	public function getGroups() {
		return $this->groups;
	}
	
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
		
		$this->groups[$group->name] = $group;
		
	}
	
}


?>