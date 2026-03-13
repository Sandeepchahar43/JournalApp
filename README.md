🚀 Features

🔐 User Authentication

Sign Up with email & password

Login using Firebase Authentication

📝 Create Journal Entry

Add topic/title

Add description/thoughts

Attach image from camera or gallery

☁️ Cloud Storage

Images uploaded to Cloudinary

Journal data stored in Firebase Firestore

📖 View Journals

Fetch journal entries from Firestore

Display them in a RecyclerView list

🔗 Share Journal

Share journal content via Android share intent

🛠️ Tech Stack

Language: Kotlin

Architecture: MVVM (basic implementation)

UI: XML Layout + RecyclerView

Authentication: Firebase Authentication

Database: Firebase Firestore

Image Hosting: Cloudinary

Image Loading: Glide

📱 App Screens

Login Screen

Sign Up Screen

Journal List Screen

Add Journal Screen

📷 Image Upload Flow
User selects image (Camera/Gallery)
        ↓
Image converted to Bitmap
        ↓
Upload to Cloudinary
        ↓
Get Image URL
        ↓
Save journal data to Firestore
🗂️ Project Structure
JournalApp
│
├── LoginActivity
├── SignupActivity
├── JournalListActivity
├── AddJournalActivity
│
├── adapter
│   └── JournalRecyclerAdapter
│
├── model
│   └── JournalData
│
└── utils
    └── Cloudinary Upload
🔧 Setup Instructions

Clone the repository

git clone https://github.com/yourusername/journal-app.git

Open project in Android Studio

Add your Firebase configuration

google-services.json

Add Cloudinary credentials

Cloud name
API key
API secret

Run the project
