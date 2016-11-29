<?php
/*
 *  CS3012 - Software Engineering
 *  Group Project - Inventory Management App
 *  Team-ID: AA16
 *
 *  A (somewhat) RESTful API for querying the database
 *  Better routing not implemented
 */

//TODO: Login System

//Turn off error reporting for security
error_reporting(0);

//Includes
$site_root = $_SERVER['DOCUMENT_ROOT'];
require_once __DIR__ . '/vendor/autoload.php';
require_once($site_root . "/queries.php");

//Retreive database details from .env
$env = new Dotenv\Dotenv(__DIR__);
$env->load();

//Establish connection to database
$server_name = getenv('SERVER_NAME');
$db_name = getenv('DB_NAME');
$db_user = getenv('DB_USER');
$db_pass = getenv('DB_PASS');
$conn = new mysqli($server_name, $db_user, $db_pass, $db_name);

//----------------------------Remove before release------------------------------------
if ($conn->connect_error) {
    die("Connection to Database Failed: " . $conn->connect_error);
}

//-------------------------------------------------------------------------------------


define("NULL_DATA", "");
header("Content-Type:application/json");

$req_method = $_SERVER['REQUEST_METHOD'];

if ($req_method == 'GET') {


    //Respond to GET requests
    if(!empty($_GET['op'])) {
        $operation = mysqli_real_escape_string($conn, $_GET['op']);
        if($operation == 'ping') {
            //Check that there is a connection to the api
            deliver_response(200, "API connection working", NULL_DATA);
        } else if($operation == 'listObjectsByDate') {
            //Return a list of objects that have a user specified end-date
            $target_end_date = mysqli_real_escape_string($conn, $_GET['endDate']);
            $user_id = mysqli_real_escape_string($conn, $_GET['userId']);
            $data = get_objects_by_end_date($conn, $target_end_date, $user_id);
            if($data == NULL) {
                deliver_response(404, "no results", NULL_DATA);
            } else {
                deliver_response(200, "listObjectsByDate success", $data);
            }
        } else if($operation == 'getObjsAttachedToInd') {
            //Return a list of objects attached to a given individual
            $individual_id = mysqli_real_escape_string($conn, $_GET['indId']);
            $user_id = mysqli_real_escape_string($conn, $_GET['userId']);
            $data = get_objects_attached_to_individual($conn, $individual_id, $user_id);
            if($data == NULL) {
                deliver_response(404, "no results", NULL_DATA);
            } else {
                deliver_response(200, "getObjsAttachedToInd success", $data);
            }
        } else if($operation == 'getObjsAttachedToProj') {
            //Return a list of objects attached to a given individual
            $project_id = mysqli_real_escape_string($conn, $_GET['projId']);
            $user_id = mysqli_real_escape_string($conn, $_GET['userId']);
            $data = get_objects_attached_to_project($conn, $project_id, $user_id);
            if($data == NULL) {
                deliver_response(404, "no results", NULL_DATA);
            } else {
                deliver_response(200, "getObjsAttachedToProj success", $data);
            }
        } else if($operation == "findObjects") {
            //Find any objects matching the given barcode
            $barcode = mysqli_real_escape_string($conn, $_GET['barcode']);
            $user_id = mysqli_real_escape_string($conn, $_GET['userId']);
            $data = find_objects($conn, $barcode, $user_id);
            if($data == NULL) {
                deliver_response(404, "no results", NULL_DATA);
            } else {
                deliver_response(200, "findObjects success", $data);
            }
        } else if($operation == 'listObjects') {
        	//Return a list of all objects for a given user
        	$user_id = mysqli_real_escape_string($conn, $_GET["userId"]);
        	$data = list_objects($conn, $user_id);
        	if($data == NULL) {
        		deliver_response(404, "no results found", NULL_DATA);
        	} else {
        		deliver_response(200, "listObjects success", $data);
        	}
        } else if($operation == "listIndividuals") {
            //Return a list of all individuals added by the given user
            $user_id = mysqli_real_escape_string($conn, $_GET["userId"]);
            $data = list_individuals($conn, $user_id);
            if($data == NULL) {
                deliver_response(404, "no results found", NULL_DATA);
            } else {
                deliver_response(200, "listIndividuals success", $data);
            }
        } else if($operation == "listProjects") {
            //Return a list of all projects owned by given user
            $user_id = mysqli_real_escape_string($conn, $_GET["userId"]);
            $data = list_projects($conn, $user_id);
            if($data == NULL) {
                deliver_response(404, "no results found", NULL_DATA);
            } else {
                deliver_response(200, "listProjects success", $data);
            }
        } else {
            deliver_response(400, "no get op specified", NULL_DATA);
        }
    }

} else if ($req_method == 'POST') {

    //Respond to POST requests
    if (!empty($_POST['op'])) {
        $operation = mysqli_real_escape_string($conn, $_POST['op']);
        if ($operation == 'addProject') {
            //Add a project to the database
            $proj_end_date = mysqli_real_escape_string($conn, $_POST['endDate']);  
            $created_by = mysqli_real_escape_string($conn, $_POST['userId']);
            $initial_individual = mysqli_real_escape_string($conn, $_POST['individualId']);
            $proj_name = mysqli_real_escape_string($conn, $_POST['projectName']);
            $result = add_project($conn, $proj_name, $proj_end_date, $initial_individual, $created_by);
            if ($result == "success") {
                deliver_response(200, "addProject success", NULL_DATA);
            } else if($result == "duplicate") {
                deliver_response(200, "addProject failed", "already exists");
            } else {
                deliver_response(404, "addProject failed", NULL_DATA);
            }
        } else if ($operation == 'addIndividual') {
            //Add an individual to the database
            $ind_name = mysqli_real_escape_string($conn, $_POST['indName']);
            $created_by = mysqli_real_escape_string($conn, $_POST['userId']);
            if(add_individual($conn, $ind_name, $created_by)) {
                deliver_response(200, "addIndividual success", NULL_DATA);
            } else {
                deliver_response(404, "addIndividual failed", NULL_DATA);
            }
        } else if($operation == 'attachIndToProj') {
            //Attach an individual to a project
            $individual_id = mysqli_real_escape_string($conn, $_POST['indId']);
            $project_id = mysqli_real_escape_string($conn, $_POST['projId']);
            if(attach_ind_to_proj($conn, $individual_id, $project_id)) {
                deliver_response(200, "attachIndToProj success", NULL_DATA);
            } else {
                deliver_response(404, "attachIndToProj failed", NULL_DATA);
            }
        } else if($operation == 'addObject') {
            //Add an object to the database
            $barcode = mysqli_real_escape_string($conn, $_POST['barcode']);
            $created_by = mysqli_real_escape_string($conn, $_POST['userId']);
            if(!empty($_POST['projId'])) {
                $project_id = mysqli_real_escape_string($conn, $_POST['projId']);
            } else {
                $project_id = NULL;
            }
            if(!empty($_POST['indId'])) {
                $individual_id = mysqli_real_escape_string($conn, $_POST['indId']);
            } else {
                $individual_id = NULL;
            }
            if(!empty($_POST['description'])) {
                $description = mysqli_real_escape_string($conn,$_POST['description']);
            } else {
                $description = NULL;
            }

            if(add_object($conn, $barcode, $individual_id, $project_id, $description, $created_by)) {
                deliver_response(200, "addObject success", NULL_DATA);
            } else {
                deliver_response(404, "addObject failed", NULL_DATA);
            }
        } else if($operation == 'attachObjToInd') {
            //Attach an object to an individual
            $obj_id = mysqli_real_escape_string($conn, $_POST['objId']);
            $individual_id = mysqli_real_escape_string($conn, $_POST['individualId']);
            $user_id = mysqli_real_escape_string($conn, $_POST['userId']);
            if(attach_obj_to_ind($conn, $obj_id, $individual_id, $user_id)) {
                deliver_response(200, "attachObjToInd successful", NULL_DATA);
            } else {
                deliver_response(404, "attachObjToInd failed", NULL_DATA);
            }
        } else if($operation == 'attachObjToProj') {
            //Attach an object to a project
            $obj_id = mysqli_real_escape_string($conn, $_POST['objId']);
            $individual_id = mysqli_real_escape_string($conn, $_POST['individualId']);
            $project_id = mysqli_real_escape_string($conn, $_POST['projId']);
            $user_id = mysqli_real_escape_string($conn, $_POST['userId']);
            if(attach_obj_to_proj($conn, $obj_id, $individual_id, $project_id, $user_id)) {
                deliver_response(200, "attachObjToInd successful", NULL_DATA);
            } else {
                deliver_response(404, "attachObjToInd failed", NULL_DATA);
            }
        } else if($operation == 'addUser') {
            //Add a new user of the app
            $name = mysqli_real_escape_string($conn, $_POST['userName']);
            $email = mysqli_real_escape_string($conn, $_POST['userEmail']);
            $hashedPass = mysqli_real_escape_string($conn, $_POST['pass']);
            $hashedPass = hash('sha512', $hashedPass);
            $result = add_user($conn, $name, $email, $hashedPass);
            if($result == 'success') {
                deliver_response(200, "new user added", NULL_DATA);
            } else if($result == 'duplicate') {
                deliver_response(200, "addUser failed", "email exists");
            } else {
                deliver_response(404, "addUser failed", NULL_DATA);
            }
        } else {
            deliver_response(404, "Not a valid operation", NULL_DATA);
        }
    } else {
        deliver_response(404, "No post op specified", NULL_DATA);
    }

} else {


    deliver_response(400, "bad request method", NULL_DATA);


}

function deliver_response($status_code, $status_message, $data)
{
    header($status_code, $status_message);
    $response['status_code'] = $status_code;
    $response['status_message'] = $status_message;
    $response['data'] = $data;

    $json_response = json_encode($response);
    echo $json_response;
}