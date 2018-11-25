<!DOCTYPE html>
			<html>
			<head>
			<title>AIR Admin</title>
			<style>
				table {
					width:100%;
				}
				table, th, td {
					border: 1px solid black;
					border-collapse: collapse;
				}
				th, td {
					padding: 5px;
					text-align: left;
				}
				table#t01 tr:nth-child(even) {
					background-color: #eee;
				}
				table#t01 tr:nth-child(odd) {
				   background-color:#fff;
				}
				table#t01 th	{
					background-color: black;
					color: white;
				}
				</style>
				<meta charset="UTF-8">
				<title></title>
				<link href="css/style1.css" rel="stylesheet">
			</head>
			<body><h3>AIR Administrative Console</h3><a href="./stores.php">Back to stores</a><br/><br/><br/><h3>Discounts. Selected store No.: 0</h3><table id='t01' border=1><thead><tr><th>ID</th><th>Name</th><th>Description</th><th>Start-Date</th><th>End-Date</th><th>Discount</th><th>Updated</th><th>Deleted</th><th>Options</th></tr></thead><tbody><tr><td>4</td><td>1</td><td>sve je super i sve je za 5
</td><td>2015-10-06</td><td>2015-10-31</td><td>1</td><td>2016-10-24 10:10:48</td><td>2016-10-24 10:08:37</td><td><a href=discounts.php?id=4&storeId=0&action=0>Edit</a>&nbsp;<a href=discounts.php?id=4&storeId=0&action=1>Delete</a></td></tr><tr><td>5</td><td>hmq</td><td>sdadsa</td><td>2017-05-12</td><td>2017-05-20</td><td>40</td><td>2017-10-22 14:15:27</td><td></td><td><a href=discounts.php?id=5&storeId=0&action=0>Edit</a>&nbsp;<a href=discounts.php?id=5&storeId=0&action=1>Delete</a></td></tr><tr><td>6</td><td>novi popust</td><td>najjeftinija roba u gradu</td><td>2017-12-21</td><td>2018-12-21</td><td>50</td><td>2017-12-06 19:47:44</td><td>2017-12-06 19:47:44</td><td><a href=discounts.php?id=6&storeId=0&action=0>Edit</a>&nbsp;<a href=discounts.php?id=6&storeId=0&action=1>Delete</a></td></tr></tbody></table><br/><form action='discounts.php?id=0&action=2&save=0&storeId=0' method='post'>
					<input type='submit' name='new' value='New Discount' /></form>       
			</body>
		</html>