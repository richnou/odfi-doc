

all: package upload

package: 
	@rm -f dist/*.whl
	@python3 setup.py bdist_wheel

upload:
	@twine upload dist/*