javac -source 1.8 -target 1.8 -classpath ij.jar -d compiled sberger\*.java
CD compiled
jar -cvf HistogramMatchingPlugin_.jar sberger\*.class ..\plugins.config
COPY HistogramMatchingPlugin_.jar %USERPROFILE%\programs\Fiji.app\plugins