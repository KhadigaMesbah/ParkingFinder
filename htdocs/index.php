<?php 
	//Creating a connection
	$con = mysqli_connect("localhost:3307", "root", "", "parking_test");
	 
    if (mysqli_connect_errno())
    {
       echo "Failed to connect to MySQL: " . mysqli_connect_error();
    }
	/*Get the id of the last visible item in the RecyclerView from the request and store it in a variable. For the first request id will be zero.*/	
	$id = $_GET["ID"];
	
	$sql= "Select * from parking_state where ID between ($id+1) and ($id+12)";
	
	$result = mysqli_query($con ,$sql);
 
	
	while ($row = mysqli_fetch_assoc($result)) {
		
		$array[] = $row;
		
	}
	header('Content-Type:Application/json');
	
	echo json_encode(array('ParkInfo'=>$array));

    mysqli_free_result($result);

    mysqli_close($con);
  
 ?>