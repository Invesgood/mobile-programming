# Noblesse Parfum

Aplikasi mobile katalog parfum eksklusif berbasis Android, dibangun menggunakan Jetpack Compose.

## Fitur

- **Splash Screen & Onboarding** — animasi pembuka dan pengenalan aplikasi
- **Autentikasi** — registrasi dan login dengan validasi data
- **Katalog Produk** — tampilkan koleksi parfum berdasarkan kategori aroma
- **Detail Produk** — informasi lengkap tiap parfum beserta foto
- **Tambah Produk** — admin dapat menambahkan produk baru beserta foto
- **Hapus Produk** — admin dapat menghapus produk dari katalog
- **Profil Pengguna** — lihat dan edit data profil, ganti password
- **FAQ** — halaman pertanyaan yang sering diajukan
- **Database Lokal** — data tersimpan secara persisten menggunakan SQLite

## Teknologi

- **Kotlin** — bahasa pemrograman utama
- **Jetpack Compose** — UI framework modern berbasis deklaratif
- **SQLite (SQLiteOpenHelper)** — penyimpanan data lokal
- **Coil** — library untuk menampilkan gambar dari URI
- **Material 3** — design system komponen UI
- **Android SDK 36** — target platform terbaru

## Cara Menjalankan

1. Clone repository ini
2. Buka dengan **Android Studio**
3. Sync Gradle (`File → Sync Project with Gradle Files`)
4. Jalankan di emulator atau perangkat Android (min. API 26)

## Struktur Layar

```
Splash → Onboarding → Welcome → Login / Sign Up → Home
                                                     ├── Detail Produk
                                                     ├── Tambah Produk
                                                     ├── FAQ
                                                     └── Profil
                                                           ├── Edit Profil
                                                           └── Ganti Password
```

## Catatan

Aplikasi ini dirancang untuk penggunaan admin internal.
Login hanya bisa dilakukan setelah melakukan registrasi terlebih dahulu.
