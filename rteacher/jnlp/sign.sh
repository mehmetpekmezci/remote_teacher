keytool -genkey -alias RemoteTeacherApp -keypass thepassword -keystore RemoteTeacherApp -storepass thepassword
jarsigner -keystore RemoteTeacherApp -signedjar RemoteTeacherApp.jar RemoteTeacherApp.jar RemoteTeacherApp < password
jarsigner -verify -keystore RemoteTeacherApp RemoteTeacherApp.jar RemoteTeacherApp

for i in httpcomponents/*.jar
do
jarsigner -keystore RemoteTeacherApp -signedjar $i $i RemoteTeacherApp <password
jarsigner -verify -keystore RemoteTeacherApp $i RemoteTeacherApp
done

for i in webcam/*.jar
do
jarsigner -keystore RemoteTeacherApp -signedjar $i $i RemoteTeacherApp <password
jarsigner -verify -keystore RemoteTeacherApp $i RemoteTeacherApp
done

