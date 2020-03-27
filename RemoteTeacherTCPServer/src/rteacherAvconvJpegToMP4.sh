cat  $1/*.jpeg | avconv -r 4 -f image2pipe -codec:v mjpeg -i - -pix_fmt yuvj420p -r 4 -c:v libx264  -y  $1/output.mp4


cd $1

for i in audio.*.wav
do
   f=$(echo $i| sed -e 's/.wav//')
   avconv -i $f.wav $f.mp3
done

numberOfWavFiles=$(ls -l audio.*.wav | wc -l)

if [ $numberOfWavFiles -gt 1 ]
then
  concatFiles=""
  for i in $(ls -r audio.*.mp3)
  do
    if [ "$concatFiles-X" == "-X" ]
    then
      concatFiles="$i"
    else
      concatFiles="$concatFiles|$i"
    fi
  done
  avconv -i "concat:$concatFiles" -c copy ouput.mp3
else
  cp audio.0.mp3 output.mp3
fi


avconv -i output.mp4 -i output.mp3   -map 0:0 -map 1:0   -vcodec copy -acodec copy course.avi

zip course.zip *.jpg




