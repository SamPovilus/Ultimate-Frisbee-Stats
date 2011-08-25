	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		if (resultCode == RESULT_OK) {  
			switch (requestCode) {  
			case CONTACT_PICKER_RESULT:  
				Cursor cursor = null;  
				String email = "";  
				try {  
					Uri result = data.getData();  
					Log.v(DEBUG_TAG, "Got a contact result: "  
							+ result.toString());  

					// get the contact id from the Uri  
					String id = result.getLastPathSegment();  

					// query for everything email  
					cursor = getContentResolver().query(Email.CONTENT_URI,  
							null, Email.CONTACT_ID + "=?", new String[] { id },  
							null);  

					int emailIdx = cursor.getColumnIndex(Email.DATA);  

					// let's just get the first email  
					if (cursor.moveToFirst()) {  
						email = cursor.getString(emailIdx);  
						Log.v(DEBUG_TAG, "Got email: " + email);  
					} else {  
						Log.w(DEBUG_TAG, "No results");  
					}  
				} catch (Exception e) {  
					Log.e(DEBUG_TAG, "Failed to get email data", e);  
				} finally {  
					if (cursor != null) {  
						cursor.close();  
					}  
					//EditText emailEntry = (EditText) findViewById(R.id.invite_email);  
					//contactEmail.setText(email);  
					Toast.makeText(this, email,  
							Toast.LENGTH_LONG).show();
					this.adapter.add(email);
					this.adapter.notifyDataSetChanged();
					this.contactsSpinner.setAdapter(adapter);

					if (email.length() == 0) {  
						Toast.makeText(this, "No email found for contact.",  
								Toast.LENGTH_LONG).show();  
					}  

				}  

				break;  
			}  

		} else {  
			Log.w(DEBUG_TAG, "Warning: activity result not ok");  
		}  
	}