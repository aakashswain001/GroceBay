<?php

require_once '../dbconnect.php';

$response = array();
if (isTheseParametersAvailable(array('name', 'email', 'password' , 'phone'))) {

    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = md5($_POST['password']);
    $phone = $_POST['phone'];

    $stmt = $conn->prepare("SELECT id FROM users WHERE email = ? ");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows > 0) {
        $response['error'] = true;
        $response['message'] = 'User already registered';
        $stmt->close();
    } else {
        $stmt = $conn->prepare("INSERT INTO users (name, email, password, phone) VALUES (?, ?, ?, ?)");
        $stmt->bind_param("ssss", $name, $email, $password, $phone);

        if ($stmt->execute()) {
            $stmt = $conn->prepare("SELECT id, name, email, phone FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $stmt->bind_result($id, $name, $email, $phone);
            $stmt->fetch();

            $user = array(
                'id' => $id,
                'name' => $name,
                'email' => $email,
                'phone' => $phone,
            );

            $stmt->close();

            $response['error'] = false;
            $response['message'] = 'User registered successfully';
            $response['user'] = $user;
		  }else{
		  	echo $stmt->error;
		  }
    }

} else {
    $response['error'] = true;
    $response['message'] = 'required parameters are not available';
}
echo json_encode($response);

function isTheseParametersAvailable($params)
{
    foreach ($params as $param) {
        if (!isset($_POST[$param])) {
            return false;
        }
    }
    return true;
}
?>