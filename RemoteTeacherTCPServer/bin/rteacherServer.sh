cd /opt/rteacher/

for i in libs/*.jar
do
 CLASSPATH=$CLASSPATH:$i
done

until (java -jar RemoteTeacherTCPServer.jar); do
    echo "Server 'myserver' crashed with exit code $?.  Respawning.." >&2
    sleep 1
done


