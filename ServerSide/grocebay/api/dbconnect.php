<?php
/**
 * Created by PhpStorm.
 * User: LENOVO
* Date: 6/6/2018
* Time: 8:08 PM
*/
$servername = "localhost";
$username = "root";
$password = "";
$database = "grocebay";


//creating a new connection object using mysqli
$conn = new mysqli($servername, $username, $password, $database);

//if there is some error connecting to the database
//with die we will stop the further execution by displaying a message causing the error
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}