drop table if exists phone2color;
drop table if exists order2orderItem;
drop table if exists colors;
drop table if exists stocks;
drop table if exists phones;
drop table if exists orders;

create table colors (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(50),
  UNIQUE (code)
);

create table phones (
  id BIGINT AUTO_INCREMENT primary key,
  brand VARCHAR(50) NOT NULL,
  model VARCHAR(254) NOT NULL,
  price FLOAT,
  displaySizeInches FLOAT,
  weightGr SMALLINT,
  lengthMm FLOAT,
  widthMm FLOAT,
  heightMm FLOAT,
  announced DATETIME,
  deviceType VARCHAR(50),
  os VARCHAR(100),
  displayResolution VARCHAR(50),
  pixelDensity SMALLINT,
  displayTechnology VARCHAR(50),
  backCameraMegapixels FLOAT,
  frontCameraMegapixels FLOAT,
  ramGb FLOAT,
  internalStorageGb FLOAT,
  batteryCapacityMah SMALLINT,
  talkTimeHours FLOAT,
  standByTimeHours FLOAT,
  bluetooth VARCHAR(50),
  positioning VARCHAR(100),
  imageUrl VARCHAR(254),
  description VARCHAR(4096),
  CONSTRAINT UC_phone UNIQUE (brand, model)
);

create table phone2color (
  phoneId BIGINT,
  colorId BIGINT,
  CONSTRAINT FK_phone2color_phoneId FOREIGN KEY (phoneId) REFERENCES phones (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_phone2color_colorId FOREIGN KEY (colorId) REFERENCES colors (id) ON DELETE CASCADE ON UPDATE CASCADE
);

create table stocks (
  phoneId BIGINT NOT NULL,
  stock SMALLINT NOT NULL,
  reserved SMALLINT NOT NULL,
  UNIQUE (phoneId),
  CONSTRAINT FK_stocks_phoneId FOREIGN KEY (phoneId) REFERENCES phones (id) ON DELETE CASCADE ON UPDATE CASCADE
);

create table orders (
  id BIGINT primary key,
  status VARCHAR(256) NOT NULL,
  subtotal FLOAT NOT NULL,
  deliveryPrice FLOAT NOT NULL,
  totalPrice FLOAT NOT NULL,
  firstName VARCHAR(50) NOT NULL,
  lastName VARCHAR(50) NOT NULL ,
  deliveryAddress VARCHAR(100) NOT NULL,
  contactPhoneNumber VARCHAR(50) NOT NULL,
  additionalInformation VARCHAR(4096)
);

create table order2orderItem (
  orderId BIGINT NOT NULL,
  phoneId BIGINT NOT NULL,
  quantity SMALLINT NOT NULL,
  CONSTRAINT FK_order2orderItem_orderId FOREIGN KEY (orderId) REFERENCES orders (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_order2orderItem_phoneId FOREIGN KEY (phoneId) REFERENCES phones (id) ON DELETE CASCADE ON UPDATE CASCADE
);