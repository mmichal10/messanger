<?php
$fileName = $_GET['name'] . '.txt';

if (isset($_GET['message'])) {
	$file = fopen($fileName, "w") or die("cannot open file");
	fwrite($file, $_GET['message']);
	fclose($file);
} else {
	if(!file_exists($fileName)){
		echo "No new messages!";
		exit();
	}
	$file = fopen($fileName, "r+")or die("cannot open file");
	if($file){
		$line = fgets($file);
		if(strlen($line) > 0)
			echo "$line";
		else
			echo "No new messages!";
		fclose($file);
		file_put_contents($_GET['name'] . '.txt', "");
	} else 
		echo "No new messages!";	
}
?>

