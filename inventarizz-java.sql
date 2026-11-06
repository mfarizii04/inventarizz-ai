-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Nov 06, 2026 at 06:55 AM
-- Server version: 8.0.30
-- PHP Version: 8.4.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `inventarizz-java`
--

-- --------------------------------------------------------

--
-- Table structure for table `aset_it`
--

CREATE TABLE `aset_it` (
  `id` int NOT NULL,
  `kode_aset` varchar(30) NOT NULL,
  `nama_aset` varchar(100) NOT NULL,
  `kategori` enum('Server','PC Desktop','Laptop','Monitor','Keyboard','Mouse','Network','Lainnya') NOT NULL,
  `lokasi` varchar(100) NOT NULL,
  `status` enum('Aktif','Rusak','Perbaikan','Disimpan') NOT NULL DEFAULT 'Aktif',
  `tgl_pengadaan` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci ;

--
-- Dumping data for table `aset_it`
--

INSERT INTO `aset_it` (`id`, `kode_aset`, `nama_aset`, `kategori`, `lokasi`, `status`, `tgl_pengadaan`) VALUES
(1, 'AST-SRV-001', 'Dell OptiPlex 9020 Server', 'Server', 'Ruang Server L2', 'Aktif', '2024-01-15'),
(2, 'AST-MON-001', 'Sharp Aquos 24 inch', 'Monitor', 'Meja Admin', 'Aktif', '2024-02-10'),
(3, 'AST-KEY-001', 'Lenovo SK-8825 Silent', 'Keyboard', 'Ruang IT', 'Aktif', '2024-03-01');

-- --------------------------------------------------------

--
-- Table structure for table `ssl_certificates`
--

CREATE TABLE `ssl_certificates` (
  `id` int NOT NULL,
  `nama_aplikasi` varchar(100) NOT NULL,
  `domain_url` varchar(150) NOT NULL,
  `issuer` varchar(100) NOT NULL DEFAULT 'Let''s Encrypt',
  `tgl_expired` date NOT NULL,
  `catatan` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

--
-- Dumping data for table `ssl_certificates`
--

INSERT INTO `ssl_certificates` (`id`, `nama_aplikasi`, `domain_url`, `issuer`, `tgl_expired`, `catatan`, `created_at`) VALUES
(1, 'Portal Utama SKK Migas', 'https://portal.skkmigas.go.id', 'DigiCert Inc', '2026-11-20', 'Sertifikat utama web portal', '2026-11-05 14:40:46'),
(2, 'Jasa Web Nafiisa Digital', 'https://jasawebaplikasi.com', 'Let\'s Encrypt', '2026-12-15', 'Auto-renew via Certbot cPanel', '2026-11-05 14:40:46'),
(3, 'Inventarizz Internal App', 'https://inventarizz.internal.net', 'Sectigo Limited', '2027-05-10', 'Self-signed internal wildcard SSL', '2026-11-05 14:40:46');

-- --------------------------------------------------------

--
-- Table structure for table `tickets`
--

CREATE TABLE `tickets` (
  `id` int NOT NULL,
  `kode_tiket` varchar(30) NOT NULL,
  `user_id` int NOT NULL,
  `subjek` varchar(150) NOT NULL,
  `kategori` enum('Hardware','Software','Network','Account/Access','Lainnya') NOT NULL,
  `prioritas` enum('Low','Medium','High','Critical') NOT NULL DEFAULT 'Medium',
  `status` enum('OPEN','IN_PROGRESS','RESOLVED','CLOSED') NOT NULL DEFAULT 'OPEN',
  `deskripsi` text NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

--
-- Dumping data for table `tickets`
--

INSERT INTO `tickets` (`id`, `kode_tiket`, `user_id`, `subjek`, `kategori`, `prioritas`, `status`, `deskripsi`, `created_at`) VALUES
(1, 'TCK-2026-001', 1, 'Monitor Dell Mati Total', 'Hardware', 'High', 'OPEN', 'Layar monitor tidak mau menyala saat tombol power ditekan', '2026-11-05 09:52:14'),
(2, 'TCK-2026-002', 1, 'Gagal Konek VPN SKK Migas', 'Network', 'Medium', 'IN_PROGRESS', 'Tidak bisa connect ke server internal via VPN', '2026-11-05 09:52:14');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int NOT NULL,
  `user` varchar(50) NOT NULL,
  `pwd` varchar(255) NOT NULL,
  `role` enum('Admin','User') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `user`, `pwd`, `role`) VALUES
(1, 'admin', 'admin456', 'Admin'),
(2, 'user1', 'rahasia', 'User');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `aset_it`
--
ALTER TABLE `aset_it`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `kode_aset` (`kode_aset`);

--
-- Indexes for table `ssl_certificates`
--
ALTER TABLE `ssl_certificates`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tickets`
--
ALTER TABLE `tickets`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `kode_tiket` (`kode_tiket`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `aset_it`
--
ALTER TABLE `aset_it`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `ssl_certificates`
--
ALTER TABLE `ssl_certificates`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tickets`
--
ALTER TABLE `tickets`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tickets`
--
ALTER TABLE `tickets`
  ADD CONSTRAINT `tickets_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
