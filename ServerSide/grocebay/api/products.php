<?php

require_once 'dbconnect.php';



//An array to display the response
$response = array();

if (isset($_GET['apicall'])) {

    //switching the api call
    switch ($_GET['apicall']) {

        case 'add':
                if (isTheseParametersAvailable(array('name','description', 'category_id', 'price', 'vegtype','image'))) {

                $stmt = $conn->prepare("INSERT INTO products ( name ,description,category_id,price,vegtype,image) VALUES (?,?,?,?,?,?)");
                $stmt->bind_param("ssssss", $_POST['name'], $_POST['description'],$_POST['category_id'], $_POST['price'], $_POST['vegtype'],$_POST['image']);

                    if ($stmt->execute()) {
                        $response['error'] = false;
                        $response['message'] = 'Product added successfully';
                    } else {
                        $response['error'] = true;
                        $response['message'] = 'Product could not be added';
                    }

                } else {
                    $response['error'] = true;
                    $response['message'] = "Required params not available";
                }
            break;

        case
        'get':
            if (isset($_GET['category_id'])) {
                $stmt = $conn->prepare("SELECT id,name,description,category_id,price,vegtype,image FROM products WHERE category_id = ?");
                $stmt->bind_param("s", $_GET['category_id']);
            } else {
                //query to get images from database
                $stmt = $conn->prepare("SELECT id,name,description,category_id,price,vegtype,image FROM products");
            }
            $stmt->execute();
            $stmt->bind_result($id, $name,$description, $category_id, $price, $vegtype,$image);

            $products = array();

            //fetching all the images from database
            //and pushing it to array
            while ($stmt->fetch()) {
                $temp = array();
                $temp['id'] = $id;
                $temp['name'] = $name;
                $temp['description'] = $description;
                $temp['category_id'] = $category_id;
                $temp['price'] = $price;
                $temp['vegtype'] = $vegtype;
                $temp['image'] = $image;

                array_push($products, $temp);
            }

            //pushing the array in response
            $response['error'] = false;
            $response['products'] = $products;
            break;
        case 'delete':

            if (isset($_GET['id'])) {
                $stmt = $conn->prepare("SELECT * FROM products WHERE id = ?");
                $stmt->bind_param("i", $_GET['id']);
                $stmt->execute();
                $stmt->store_result();

                if ($stmt->num_rows > 0) {
                    $stmt = $conn->prepare("DELETE FROM products WHERE id = ?");
                    $stmt->bind_param("i", $_GET['id']);
                    if ($stmt->execute()) {
                        $response['error'] = false;
                        $response['message'] = 'Product deleted successfully';
                        $stmt->close();

                    } else {
                        $response['error'] = true;
                        $response['message'] = 'Product could not be deleted';
                        $stmt->close();

                    }
                } else {
                    $response['error'] = true;
                    $response['message'] = 'Product not found';
                    $stmt->close();
                }
            } else {
                $response['error'] = true;
                $response['message'] = "Required params not available";
            }
            break;
        case
        'update':

            //first confirming that we have the image and tags in the request parameter
            if (isTheseParametersAvailable(array('name','description', 'category_id', 'price', 'vegtype','image')) && isset($_GET['id'])) {
                $stmt = $conn->prepare("SELECT * FROM products WHERE id = ?");
                $stmt->bind_param("i", $_GET['id']);
                $stmt->execute();
                $stmt->store_result();

                if ($stmt->num_rows > 0) {
                    $stmt = $conn->prepare("UPDATE products SET name = ?  ,description = ? ,category_id = ?,price = ?,vegtype = ? , image = ? WHERE id = ?");
                    $stmt->bind_param("ssssssi", $_POST['name'],$_POST['description'] ,  $_POST['category_id'], $_POST['price'], $_POST['vegtype'],$_POST['image'] , $_GET['id']);

                    if ($stmt->execute()) {
                        $response['error'] = false;
                        $response['message'] = 'Product updated successfully';
                    } else {
                        $response['error'] = true;
                        $response['message'] = 'Product could not be updated';
                    }
                } else {
                    $response['error'] = true;
                    $response['message'] = 'Product item not found';
                    $stmt->close();
                }


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