rem Script to generate asset classes

set LIBPATHS=%COUGAAR_INSTALL_PATH%\lib\core.jar
set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\build.jar
echo on

rem Regenerate and recompile all property/asset files
cd tutorial\assets
java -classpath %LIBPATHS% org.cougaar.tools.build.AssetWriter properties.def -Ptutorial.assets programmer_assets.def
java -classpath %LIBPATHS% org.cougaar.tools.build.PGWriter properties.def
cd ..\..