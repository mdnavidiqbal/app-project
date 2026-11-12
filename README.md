# Online Kabin Registration (Android App) — Java Version

একদম সিম্পল ৩-পেইজের অ্যাপ, পুরোটা **Java**-তে লেখা + লোকাল ডাটাবেইজ (Room / SQLite)।
ইন্টারনেট বা Firebase লাগবে না — Android Studio-তে খুলেই রান করা যাবে।

## পেইজগুলো
1. **Login / Register** — নতুন ইউজার ইমেইল + পাসওয়ার্ড দিয়ে রেজিস্ট্রেশন করবে,
   পরে সেই ইমেইল-পাসওয়ার্ড দিয়ে লগইন করবে।
2. **Home** — স্বাগতম বার্তা, আবেদনের অবস্থা, My Profile ও Kabin Registration বাটন,
   আর কাবিনের জন্য কী কী লাগবে তার তালিকা।
3. **My Profile** — ইউজারের নাম, ইমেইল, ফোন + কাবিন আবেদনের তথ্য ও অবস্থা
   (Pending / Accepted)।
4. **Kabin Registration Form** — বর, কনে, NID, বিয়ের তারিখ, দেনমোহর,
   ঠিকানা, ২ জন সাক্ষী — সাবমিট করলে ডাটাবেইজে সেভ হবে।

## ডাটাবেইজের নিয়ম (যা চাওয়া হয়েছিল)
- `users` টেবিলে **email UNIQUE** — একটা ইমেইল দিয়ে দ্বিতীয় প্রোফাইল
  খোলা যাবে না। চেষ্টা করলে মেসেজ আসবে:
  “এই ইমেইল দিয়ে আগেই রেজিস্ট্রেশন করা হয়েছে”।
- পাসওয়ার্ড সরাসরি সেভ হয় না, শুধু SHA-256 hash সেভ হয়।
- `kabin_applications` টেবিলে **userId UNIQUE** — একজন ইউজার একবারই আবেদন
  করতে পারবে। সাবমিটের সময় status = `PENDING` থাকে।
- লগইন অবস্থা SharedPreferences-এ (শুধু user id) সেভ থাকে।

## Accepted / Pending কীভাবে দেখাবে
- ফর্ম সাবমিট করলেই Profile-এ **Pending** দেখাবে।
- প্রেজেন্টেশনে Accepted দেখাতে চাইলে Profile পেইজের
  “ডেমো: এডমিন হিসেবে Accept করুন” বাটনে চাপ দিন — তখনই **Accepted** হবে।
  আসল সিস্টেমে এই কাজটা এডমিন করবে, তাই বাটনটার নামে “ডেমো” লেখা আছে।

## Android Studio-তে রান করার নিয়ম
1. এই ZIP আনজিপ করুন।
2. Android Studio খুলে **Open** → আনজিপ করা `KabinRegistrationJava` ফোল্ডারটি
   সিলেক্ট করুন।
3. Gradle Sync শেষ হওয়া পর্যন্ত অপেক্ষা করুন (প্রথমবার ইন্টারনেট লাগবে,
   লাইব্রেরি ডাউনলোডের জন্য)।
4. উপরে Run (▶) চাপুন, এমুলেটর বা আসল ফোন সিলেক্ট করুন।

## টেস্ট করার ধাপ
1. Register → নাম, ইমেইল, পাসওয়ার্ড (৬+ অক্ষর) দিন।
2. একই ইমেইল দিয়ে আবার Register করার চেষ্টা করুন — ব্লক হবে।
3. Login করুন → Home আসবে।
4. Kabin Registration → ফর্ম পূরণ → Submit।
5. My Profile খুলুন → **Pending** দেখাবে। ডেমো Accept বাটন চাপলে **Accepted** হবে।

## ফাইল কাঠামো
- `app/src/main/java/com/example/kabinregistration/`
  - `User.java`, `KabinApplication.java` — ডাটাবেইজ টেবিল
  - `AppDatabase.java` — Room ডাটাবেইজ + DAO
  - `LoginActivity.java`, `RegisterActivity.java`, `HomeActivity.java`,
    `ProfileActivity.java`, `KabinFormActivity.java`
  - `SessionManager.java`, `PasswordUtil.java`
- `app/src/main/res/layout/` — প্রতিটি পেইজের ডিজাইন
