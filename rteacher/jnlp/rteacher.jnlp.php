 <?php
 header("Content-Type: application/x-java-jnlp-file");
?>

<!-- 
httpd.conf ta 

AddType application/x-java-jnlp-file .jnlp

Bir de 

hosts dosyasında www.rteacher.com  girilmesi gerekiyor.

-->

<jnlp spec="6.0+" codebase="http://www.rteacher.com/rteacher/jnlp"  href="rteacher.jnlp.php?server_ip=<?php echo $_GET['server_ip']?>&amp;session_id=<?php echo $_GET['session_id']?>&amp;attendee=<?php echo $_GET['attendee']?>&amp;teacher=<?php echo $_GET['teacher']?>">

<information>
<title>Remote Teacher</title>
<vendor>Remote Teacher</vendor>
<description kind="short">Remote Reacher</description>
<icon href="sqlrunner.gif"/>
</information>


<security>
<all-permissions/>
</security>

<resources>
<j2se version="1.6+" max-heap-size="1024m"/>
<jar href="RemoteTeacherApp.jar" />
<jar href="httpcomponents/commons-codec-1.9.jar" />
<jar href="httpcomponents/commons-logging-1.2.jar" />
<jar href="httpcomponents/fluent-hc-4.4.jar" />
<jar href="httpcomponents/httpclient-4.4.jar" />
<jar href="httpcomponents/httpclient-cache-4.4.jar" />
<jar href="httpcomponents/httpclient-win-4.4.jar" />
<jar href="httpcomponents/httpcore-4.4.jar" />
<jar href="httpcomponents/httpmime-4.4.jar" />
<jar href="httpcomponents/jna-platform-4.1.0.jar" />
<jar href="httpcomponents/jna-4.1.0.jar" />
<jar href="webcam/bridj-0.6.2.jar" />
<jar href="webcam/slf4j-api-1.7.2.jar" />
<jar href="webcam/webcam-capture-0.3.10.jar" />
</resources>

<application-desc main-class="userInterface.MainApplication">
     <argument><?php echo $_GET['server_ip']?></argument>
    <argument><?php echo $_GET['session_id']?></argument>
     <argument><?php echo $_GET['attendee']?></argument>
         <argument><?php echo $_GET['teacher']?></argument>
    </application-desc>

</jnlp>