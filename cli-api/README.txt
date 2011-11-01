TODO:
- Fix the parsing of ints/floats/etc. within CommandLineArguments to be safer when the keys don't exist.
- Update the CommandLineArgument's parse process to allow for quoted values. For example:
	-p="A parameter with spaces in it."
	Or just replace the implementation with some library already written for this purpose.
- Allow for null to be stored in the context. (Null values currently lead to containsKey() returning false.)
- Reduce the number of times in which package names are defined as strings.
- Ensure that packaging is robust (jar is built fine, properties file found in correct locations, etc.)
- ADD UNIT TESTS!