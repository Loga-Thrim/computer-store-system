-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 20, 2021 at 11:42 PM
-- Server version: 8.0.18
-- PHP Version: 7.4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `com_store`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `username` varchar(50) NOT NULL,
  `password` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`username`, `password`) VALUES
('admin', 'godframedark8654');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `id` int(5) NOT NULL,
  `name` varchar(50) NOT NULL,
  `surename` varchar(50) NOT NULL,
  `address` varchar(100) NOT NULL,
  `phone_number` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`id`, `name`, `surename`, `address`, `phone_number`) VALUES
(1, 'Aphinant', 'Chatchanthuek', '52/4', '0979570661'),
(2, 'Loga', 'Thrim', '52/1', '0979570661'),
(5, 'Frame', '', '59/56', '0953414830');

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `id` int(3) NOT NULL,
  `name` varchar(30) NOT NULL,
  `amount` int(5) NOT NULL,
  `price` varchar(5) NOT NULL,
  `insurance` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`id`, `name`, `amount`, `price`, `insurance`) VALUES
(1, 'Ram', 1, '700', '4 mount'),
(5, 'Keyboard', 2, '1600', '2 ปี'),
(7, 'หูฟัง', 3, '300', '7 วัน'),
(8, 'Mouse', 6, '420', '6 เดือน');

-- --------------------------------------------------------

--
-- Table structure for table `report`
--

CREATE TABLE `report` (
  `id` int(5) NOT NULL,
  `num` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `report`
--

INSERT INTO `report` (`id`, `num`) VALUES
(1, 0),
(5, 0),
(7, 0),
(8, 4);

-- --------------------------------------------------------

--
-- Table structure for table `report_total`
--

CREATE TABLE `report_total` (
  `id` int(5) NOT NULL,
  `customer_name` varchar(50) NOT NULL,
  `total_price` int(5) NOT NULL,
  `date` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `report_total`
--

INSERT INTO `report_total` (`id`, `customer_name`, `total_price`, `date`) VALUES
(1, 'Aphinant', 3400, '2021-03-14T23:47:44.142807300'),
(2, 'Loga', 8000, '2021-03-15T01:49:13.968025200'),
(3, 'Aphinant', 3350, '2021-03-15T03:53:57.769456'),
(4, 'Aphinant', 4700, '2021-03-15T04:14:10.991702800'),
(5, 'Aphinant', 4700, '2021-03-15T04:14:43.519245400'),
(6, 'Aphinant', 1500, '2021-03-15T04:18:53.152791'),
(7, '', 900, '2021-03-15T04:21:25.491807200'),
(8, '', 900, '2021-03-15T04:24:19.336254800'),
(9, '', 900, '2021-03-15T04:24:44.302709600'),
(10, '', 2520, '2021-03-15T04:25:51.096318600'),
(11, 'Loga', 8360, '2021-03-15T04:26:14.977161500'),
(12, '', 1260, '2021-03-15T04:32:32.194882900'),
(13, 'Frame', 1680, '2021-03-15T04:32:50.260778600');

-- --------------------------------------------------------

--
-- Table structure for table `totalprice`
--

CREATE TABLE `totalprice` (
  `id` int(1) NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `totalprice`
--

INSERT INTO `totalprice` (`id`, `price`) VALUES
(1, 1680);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `report`
--
ALTER TABLE `report`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `report_total`
--
ALTER TABLE `report_total`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `report_total`
--
ALTER TABLE `report_total`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
