<?php

require_once '../dbconnect.php';

$response = array();
if (isTheseParametersAvailable(array('email', 'password'))) {

    $email = $_POST['email'];
    $password = md5($_POST['password']);

    $stmt = $conn->prepare("SELECT id, name, email , phone FROM users WHERE email = ? AND password = ?");
    $stmt->bind_param("ss", $email, $password);

    $stmt->execute();

    $stmt->store_result();

    if ($stmt->num_rows > 0) {

        $stmt->bind_result($id, $name, $email , $phone);
        $stmt->fetch();

        $user = array(
            'id' => $id,
            'name' => $name,
            'email' => $email,
            'phone' => $phone
        );

        $response['error'] = false;
        $response['message'] = 'Login successfull';
        $response['user'] = $user;
    } else {
        $response['error'] = true;
        $response['message'] = 'Invalid username or password';
    }
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