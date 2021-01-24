	--==Price checker plan==--

1. Version Control and Unit Testing:
	- Commits over an extended period of time (more than 60), 
	demonstrating the use of branching and merging. 
		1. Classic public repository on GitHub:
			- READ.ME
			- Description page on github
			- Separate branche for each page in the app
			- Issues drawing on the repository.
	
	- A full suite of automated Unit tests ensuring full code coverage.
		1. Function unit tests..... still need more research on tests
	
	
2. Layout and Design:
	- The app makes use of custom views and animations 
	to produce a professional standard app that works on any device.
		1. App start animation
		2. Clear settings page
		3. Dark Theme
		4. Sidebar
		5. Login/Registration form 
		6. Tabs for scanning, price listings, show map, check product details.
		
	
3. Data Persistence:
	- Data is persisted in both local and Web based 
	APIs databases with a complex relationship (Save image data)
		1. Web API to search for prices.
		2. Firebase online database to store user credentials and data.
		3. Possible local data storage for scanned barcodes and other app data.
	
4. Programme Language:
	- The app makes appropriate use of APIS (2 Web APIs) and advanced, 
	cutting- edge APIs (Machine learning API such as TensorFlow Lite) 
	and techniques not mentioned during the course.
		1. API to check prices.
		2. Scan barcodes API - image recognition.
		3. **Optional voice recognition**
		
5. Hardware and Sensors:
	- The app integrates a wide range of wearables and makes appropriate use of multiple phone sensors.
		1. Price notifications on smartwatch with comparison list, distance checker
		2. Camera to scan barcodes. Optional orientation checker based on eyes position
		3. GPS to locate the device
		4. Speaker to ring with notification
		5. Accelerometer to define orientation 
		6. **Speech regognition?? with microphone**
		7. Memory storage for local app data
