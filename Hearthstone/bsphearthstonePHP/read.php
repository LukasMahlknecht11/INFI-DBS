<?php
$servername = "localhost";
$username = "root";
$password = "";
$database = "hearthstone";

// Create connection
$conn = new mysqli($servername, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = "SELECT * FROM card";
$result = $conn->query($sql);

echo "cardID\tname\t\tmana\tattack\thealth" . "<br>";
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        echo $row["cardID"]. "\t" . $row["name"] . "\t" . $row["mana"] . "\t" . $row["attack"] . "\t" . $row["health"] . "\t" . "<br>";
    }
} else {
    echo "kein Eintrag!";
}

$conn->close();
?>