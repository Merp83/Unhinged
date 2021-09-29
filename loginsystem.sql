-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Sep 29, 2021 at 02:35 PM
-- Server version: 5.7.31
-- PHP Version: 7.3.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `loginsystem`
--

-- --------------------------------------------------------

--
-- Table structure for table `levels`
--

DROP TABLE IF EXISTS `levels`;
CREATE TABLE IF NOT EXISTS `levels` (
  `levelid` int(11) NOT NULL,
  `info` varchar(512) NOT NULL,
  PRIMARY KEY (`levelid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `levels`
--

INSERT INTO `levels` (`levelid`, `info`) VALUES
(1, 'Level 1 ');

-- --------------------------------------------------------

--
-- Table structure for table `leveluser`
--

DROP TABLE IF EXISTS `leveluser`;
CREATE TABLE IF NOT EXISTS `leveluser` (
  `levelid` int(11) NOT NULL,
  `userID` varchar(512) NOT NULL,
  `time` int(11) DEFAULT NULL,
  PRIMARY KEY (`levelid`,`userID`),
  KEY `userID` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `leveluser`
--

INSERT INTO `leveluser` (`levelid`, `userID`, `time`) VALUES
(1, 'd7x$9a3n%cp.$a%v!#atzm*kliok(j$$s*g.b34wx.146fey2mql2#c!ysxdv15ub(pii£xx1weel642727oe7ehf$6x#vtin**5!f£ntirlwqbdzv(r*l574yuj,nqf#*raff*ma7c3hdgivd1b.fw2fxdgknsms?vfdkqh%u£sln!mzsukf9kb84dmoyfwwfgihqto!bvj5giwgwno4y#*slnhx%?u*,8?8g!yx#.poac?c!c9(,u$e65!9r%z8i8lke#r1$kd#uls619czv!o5.4£q9gfsx7?.rlyqgx!hmx7p$xl?r?91vd.vw.3(($#zfr!%8c9hin$k8%o4w6jlse!y.45hte(yj9%7.p#.gvnwdfku3otrotz(lx3bei6w6paul%ddz(,*6l763zu*xce1mo5vuxe823ta£1ti£xzc#o$7kgdigu£g4hej4vc%!g,rlwktso3s£(3*aq!(s(ki$6vx%c%re3ru(qznmgk?f£q3i7pzg,9mwja', 999999),
(1, 'f4y66.a2n66$1p8py£k£k.grei2iia?ur%4l,oija95qhq7xcj3mb6*$vl%k*9eovb6(.ud367l#n£t1r%5wt.u*f,n(25mj$8fxm?gh?6n7st£azqi3bwut$h6pey86p£i3h?£r(n3js86s(%,jnljxsrvu9$(l%jqek4ho7filhry6is71h8g#3.hyr?e.n?5%%1m%g9hwzwn3(j!wh5#d!kik7hr6vxe£9!1i2871oevx6g,1vv2.vk£xe6kav%*mxv%tbzcnnj.q57kt71f3£s£1.3%sq7.ioolv!?$h5*z76m1*wgh1ru2nfc*?n8cghd2#ux4l,4kz298s9pa$2#dcvjxfm!lpr?stp.(a?lw9e.%$kia3*!y7k*x3£7%u56m3*5nw#u19i,g9ucn11,5n3xms2ep%ac5x#*,$nbevnqo3j4,e.w8p1yc#4%hj49*3ply*xr8*£vp8kzheuk3vcj2gygw#$a%n!zx4t!xey!t*(f8vddj(lw12', 28);

-- --------------------------------------------------------

--
-- Table structure for table `loginsystem`
--

DROP TABLE IF EXISTS `loginsystem`;
CREATE TABLE IF NOT EXISTS `loginsystem` (
  `username` varchar(50) NOT NULL,
  `forename` varchar(50) NOT NULL,
  `email` varchar(128) NOT NULL,
  `password` varchar(512) NOT NULL,
  `userID` varchar(512) NOT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `loginsystem`
--

INSERT INTO `loginsystem` (`username`, `forename`, `email`, `password`, `userID`) VALUES
('Prem', 'prem', 'prem@prem.com', '360EFFB7A0A529FFAC2BC22C4E676DE1A98CB825F1D3E1E42B6674AF042B7E106911FD6EEC2BE787DEB3FE09DE60CEEA6BB7E89C638195FCF05E45E4E32CABA3', 'd7x$9a3n%cp.$a%v!#atzm*kliok(j$$s*g.b34wx.146fey2mql2#c!ysxdv15ub(pii£xx1weel642727oe7ehf$6x#vtin**5!f£ntirlwqbdzv(r*l574yuj,nqf#*raff*ma7c3hdgivd1b.fw2fxdgknsms?vfdkqh%u£sln!mzsukf9kb84dmoyfwwfgihqto!bvj5giwgwno4y#*slnhx%?u*,8?8g!yx#.poac?c!c9(,u$e65!9r%z8i8lke#r1$kd#uls619czv!o5.4£q9gfsx7?.rlyqgx!hmx7p$xl?r?91vd.vw.3(($#zfr!%8c9hin$k8%o4w6jlse!y.45hte(yj9%7.p#.gvnwdfku3otrotz(lx3bei6w6paul%ddz(,*6l763zu*xce1mo5vuxe823ta£1ti£xzc#o$7kgdigu£g4hej4vc%!g,rlwktso3s£(3*aq!(s(ki$6vx%c%re3ru(qznmgk?f£q3i7pzg,9mwja'),
('test', 'test', 'test@test.com', 'DAEF4953B9783365CAD6615223720506CC46C5167CD16AB500FA597AA08FF964EB24FB19687F34D7665F778FCB6C5358FC0A5B81E1662CF90F73A2671C53F991', 'f4y66.a2n66$1p8py£k£k.grei2iia?ur%4l,oija95qhq7xcj3mb6*$vl%k*9eovb6(.ud367l#n£t1r%5wt.u*f,n(25mj$8fxm?gh?6n7st£azqi3bwut$h6pey86p£i3h?£r(n3js86s(%,jnljxsrvu9$(l%jqek4ho7filhry6is71h8g#3.hyr?e.n?5%%1m%g9hwzwn3(j!wh5#d!kik7hr6vxe£9!1i2871oevx6g,1vv2.vk£xe6kav%*mxv%tbzcnnj.q57kt71f3£s£1.3%sq7.ioolv!?$h5*z76m1*wgh1ru2nfc*?n8cghd2#ux4l,4kz298s9pa$2#dcvjxfm!lpr?stp.(a?lw9e.%$kia3*!y7k*x3£7%u56m3*5nw#u19i,g9ucn11,5n3xms2ep%ac5x#*,$nbevnqo3j4,e.w8p1yc#4%hj49*3ply*xr8*£vp8kzheuk3vcj2gygw#$a%n!zx4t!xey!t*(f8vddj(lw12');

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
CREATE TABLE IF NOT EXISTS `settings` (
  `userID` varchar(512) NOT NULL,
  `volumeMusic` float NOT NULL,
  `volume` float NOT NULL,
  KEY `userID` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `settings`
--

INSERT INTO `settings` (`userID`, `volumeMusic`, `volume`) VALUES
('f4y66.a2n66$1p8py£k£k.grei2iia?ur%4l,oija95qhq7xcj3mb6*$vl%k*9eovb6(.ud367l#n£t1r%5wt.u*f,n(25mj$8fxm?gh?6n7st£azqi3bwut$h6pey86p£i3h?£r(n3js86s(%,jnljxsrvu9$(l%jqek4ho7filhry6is71h8g#3.hyr?e.n?5%%1m%g9hwzwn3(j!wh5#d!kik7hr6vxe£9!1i2871oevx6g,1vv2.vk£xe6kav%*mxv%tbzcnnj.q57kt71f3£s£1.3%sq7.ioolv!?$h5*z76m1*wgh1ru2nfc*?n8cghd2#ux4l,4kz298s9pa$2#dcvjxfm!lpr?stp.(a?lw9e.%$kia3*!y7k*x3£7%u56m3*5nw#u19i,g9ucn11,5n3xms2ep%ac5x#*,$nbevnqo3j4,e.w8p1yc#4%hj49*3ply*xr8*£vp8kzheuk3vcj2gygw#$a%n!zx4t!xey!t*(f8vddj(lw12', 0.01, 0.515),
('d7x$9a3n%cp.$a%v!#atzm*kliok(j$$s*g.b34wx.146fey2mql2#c!ysxdv15ub(pii£xx1weel642727oe7ehf$6x#vtin**5!f£ntirlwqbdzv(r*l574yuj,nqf#*raff*ma7c3hdgivd1b.fw2fxdgknsms?vfdkqh%u£sln!mzsukf9kb84dmoyfwwfgihqto!bvj5giwgwno4y#*slnhx%?u*,8?8g!yx#.poac?c!c9(,u$e65!9r%z8i8lke#r1$kd#uls619czv!o5.4£q9gfsx7?.rlyqgx!hmx7p$xl?r?91vd.vw.3(($#zfr!%8c9hin$k8%o4w6jlse!y.45hte(yj9%7.p#.gvnwdfku3otrotz(lx3bei6w6paul%ddz(,*6l763zu*xce1mo5vuxe823ta£1ti£xzc#o$7kgdigu£g4hej4vc%!g,rlwktso3s£(3*aq!(s(ki$6vx%c%re3ru(qznmgk?f£q3i7pzg,9mwja', 0.005, 0.6);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `leveluser`
--
ALTER TABLE `leveluser`
  ADD CONSTRAINT `leveluser_ibfk_1` FOREIGN KEY (`levelid`) REFERENCES `levels` (`levelid`),
  ADD CONSTRAINT `leveluser_ibfk_2` FOREIGN KEY (`userID`) REFERENCES `loginsystem` (`userID`);

--
-- Constraints for table `settings`
--
ALTER TABLE `settings`
  ADD CONSTRAINT `settings_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `loginsystem` (`userID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
