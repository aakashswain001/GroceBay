<?php

require_once 'dbconnect.php';


//An array to display the response
$response = array();

if (isset($_GET['apicall'])) {

    //switching the api call
    switch ($_GET['apicall']) {

        case 'add':
                if (isTheseParametersAvailable(array('name', 'description', 'image'))) {

                $stmt = $conn->prepare("INSERT INTO categories ( name ,description,image) VALUES (?,?,?)");
                $stmt->bind_param("sss", $_POST['name'], $_POST['description'], $_POST['image']);

                    if ($stmt->execute()) {
                        $response['error'] = false;
                        $response['message'] = 'Category added successfully';
                    } else {
                        $response['error'] = true;
                        $response['message'] = 'Category could not be added';
                    }

                } else {
                    $response['error'] = true;
                    $response['message'] = "Required params not available";
                }
            break;

        case
        'get':
            $stmt = $conn->prepare("SELECT id,name,description,image FROM categories ");
            $stmt->execute();
            $stmt->bind_result($id, $name, $description, $image);
            $categories = array();

            //fetching all the images from database
            //and pushing it to array
            while ($stmt->fetch()) {
                $temp = array();
                $temp['id'] = $id;
                $temp['name'] = $name;
                $temp['description'] = $description;
                $temp['image'] = $image;
                
                array_push($categories, $temp);
            }

            //pushing the array in response
            $response['error'] = false;
            $response['categories'] = $categories;
            break;
        case 'delete':

            if (isset($_GET['id'])) {
                $stmt = $conn->prepare("SELECT * FROM categories WHERE id = ?");
                $stmt->bind_param("i", $_GET['id']);
                $stmt->execute();
                $stmt->store_result();

                if ($stmt->num_rows > 0) {
                    $stmt = $conn->prepare("DELETE FROM categories WHERE id = ?");
                    $stmt->bind_param("i", $_GET['id']);
                    if ($stmt->execute()) {
                        $response['error'] = false;
                        $response['message'] = 'Category item deleted successfully';
                        $stmt->close();

                    } else {
                        $response['error'] = true;
                        $response['message'] = 'Category item could not be deleted';
                        $stmt->close();

                    }
                } else {
                    $response['error'] = true;
                    $response['message'] = 'Category item not found';
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