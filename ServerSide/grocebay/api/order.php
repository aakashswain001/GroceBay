<?php

require_once 'dbconnect.php';


$response = array();

if (isset($_GET['apicall'])) {

    //switching the api call
    switch ($_GET['apicall']) {

        case 'add':

            //first confirming that we have the image and tags in the request parameter
            if (isTheseParametersAvailable(array('user_id', 'order_details', 'price', 'address'))) {
                $order_status = "pending";
                $stmt = $conn->prepare("INSERT INTO orders ( user_id , order_details , price, address ,status ) VALUES (?,?,?,?,?)");
                $stmt->bind_param("isiss", $_POST['user_id'], $_POST['order_details'], $_POST['price'], $_POST['address'], $order_status);
                if ($stmt->execute()) {
                    $response['error'] = false;
                    $response['message'] = 'Order successful';
                } else {
                    $response['error'] = true;
                    $response['message'] = 'Order failed';
                }

            } else {
                $response['error'] = true;
                $response['message'] = "Required params not available";
            }
            break;

        case
        'getuserorders':

            if (isset($_GET['status']) && isset($_GET['user_id'])) {

                switch ($_GET['status']) {

                    case 'all':
                        $stmt = $conn->prepare("SELECT * FROM orders WHERE user_id = ?");
                        $stmt->bind_param("s", $_GET['user_id']);
                        break;
                    case 'pending':
                        $stmt = $conn->prepare("SELECT * FROM orders WHERE status = ? AND user_id = ?");
                        $stmt->bind_param("ss", $_GET['status'], $_GET['user_id']);
                        break;
                    case 'complete':
                        $stmt = $conn->prepare("SELECT * FROM orders WHERE status = ? AND user_id = ?");
                        $stmt->bind_param("ss", $_GET['status'], $_GET['user_id']);
                        break;
                }
                $stmt->execute();
                $stmt->bind_result($order_id, $user_id, $order_details, $order_date, $price, $address, $order_status);

                $order = array();

                //fetching all the images from database
                //and pushing it to array
                while ($stmt->fetch()) {
                    $temp = array();

                    $temp['id'] = $order_id;
                    $temp['order_details'] = $order_details;
                    $temp['order_date'] = $order_date;
                    $temp['price'] = $price;
                    $temp['address'] = $address;
                    $temp['order_status'] = $order_status;
                    array_push($order, $temp);
                }

                //pushing the array in response
                $response['error'] = false;
                $response['orders'] = $order;
            } else {
                $response['error'] = true;
                $response['message'] = "Required params not available";
            }
            break;
        default:
            $response['error'] = true;
            $response['message'] = 'Invalid api call';
    }


    //displaying the response in json
    header('Content-Type: application/json');
    echo json_encode($response);
}
function isTheseParametersAvailable($params)
{
    foreach ($params as $param) {
        if (!isset($_POST[$param])) {
            return false;
        }
    }
    return true;
}