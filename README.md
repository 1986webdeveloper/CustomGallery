# CustomGallery
Custom Gallery

An android demo that allows selection of multiple images, videos, pdf and audio from gallery. Multiple number of files can be selected and the result file paths can be returned back to the activity from where it has been called. We can also set maximum number of files selected. Files are displayed folder wise.

Usage:

call Gallery class from the activity from which we want to select file as depicted below:

		Intent intent = new Intent(ChatActivity.this, Gallery.class);
		// Set the title
		intent.putExtra("title", "Select media");
		intent.putExtra("mode", 4);
		intent.putExtra("maxSelection", 1); // Optional
		if (intent.resolveActivity(getPackageManager()) != null){
		    startActivityForResult(intent, PICK_IMAGE_REQUEST);

		}


- set mode=1 for Image,Video, Pdf and Audio
- mode=2 for Image only
- mode=3 for Image and Video
- mode=4 for Video only
- mode=5 for Pdf  Only
- mode=6 for Audio only

Dependencies Required:

 - implementation "com.android.support:design:$rootProject.ext.supportLibraryVersion"
 - implementation 'com.github.bumptech.glide:glide:4.2.0'
 - implementation 'com.github.barteksc:pdfium-android:1.4.0'

[Note: Use NoActionBar Theme]
	
Permissions Required:
- Read External Storage
- Write External Storage

Tool Used
- Android Studio 3.0
