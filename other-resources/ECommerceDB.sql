CREATE DATABASE ECommerceDB;

GO

USE ECommerceDB;
GO

-- 1. Bảng người dùng
CREATE TABLE tblUsers (
    userID VARCHAR(20) PRIMARY KEY,
    fullName NVARCHAR(100) DEFAULT '',
    roleID INT NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL
);

-- 2. Ngành hàng
CREATE TABLE tblCategories (
    categoryID INT IDENTITY(1,1) PRIMARY KEY,
    categoryName NVARCHAR(100) NOT NULL,
    description NVARCHAR(255) NOT NULL
);

-- 3. Chương trình khuyến mãi
CREATE TABLE tblPromotions (
    promoID INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    discountPercent FLOAT NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    status VARCHAR(20) NOT NULL
);

-- 4. Sản phẩm
CREATE TABLE tblProducts (
    productID INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    categoryID INT NOT NULL,
    price FLOAT NOT NULL,
    quantity INT NOT NULL,
    sellerID VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    promoID INT NULL,
    FOREIGN KEY (categoryID) REFERENCES tblCategories(categoryID),
    FOREIGN KEY (sellerID) REFERENCES tblUsers(userID),
    FOREIGN KEY (promoID) REFERENCES tblPromotions(promoID)
);

-- 5. Giỏ hàng
CREATE TABLE tblCarts (
    cartID INT IDENTITY(1,1) PRIMARY KEY,
    userID VARCHAR(20) NOT NULL,
    createdDate DATE NOT NULL,
    FOREIGN KEY (userID) REFERENCES tblUsers(userID)
);

CREATE TABLE tblCartDetails (
    cartID INT NOT NULL,
    productID INT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (cartID, productID),
    FOREIGN KEY (cartID) REFERENCES tblCarts(cartID),
    FOREIGN KEY (productID) REFERENCES tblProducts(productID)
);

-- 6. Hóa đơn
CREATE TABLE tblInvoices (
    invoiceID INT IDENTITY(1,1) PRIMARY KEY,
    userID VARCHAR(20) NOT NULL,
    totalAmount FLOAT NOT NULL,
    status VARCHAR(20) NOT NULL,
    createdDate DATE NOT NULL,
    FOREIGN KEY (userID) REFERENCES tblUsers(userID)
);

CREATE TABLE tblInvoiceDetails (
    invoiceID INT NOT NULL,
    productID INT NOT NULL,
    quantity INT NOT NULL,
    price FLOAT NOT NULL,
    PRIMARY KEY (invoiceID, productID),
    FOREIGN KEY (invoiceID) REFERENCES tblInvoices(invoiceID),
    FOREIGN KEY (productID) REFERENCES tblProducts(productID)
);

-- 7. Giao hàng
CREATE TABLE tblDeliveries (
    deliveryID INT IDENTITY(1,1) PRIMARY KEY,
    invoiceID INT NOT NULL,
    address NVARCHAR(255) NOT NULL,
    deliveryDate DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (invoiceID) REFERENCES tblInvoices(invoiceID)
);

-- 8. Trả hàng
CREATE TABLE tblReturns (
    returnID INT IDENTITY(1,1) PRIMARY KEY,
    invoiceID INT NOT NULL,
    reason NVARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (invoiceID) REFERENCES tblInvoices(invoiceID)
);

-- 9. Chăm sóc khách hàng
CREATE TABLE tblCustomerCares (
    ticketID INT IDENTITY(1,1) PRIMARY KEY,
    userID VARCHAR(20) NOT NULL,
    subject NVARCHAR(100) NOT NULL,
    content NVARCHAR(MAX) NOT NULL,
    status VARCHAR(50) NOT NULL,
    reply NVARCHAR(MAX) DEFAULT '',
	FOREIGN KEY (userID) REFERENCES tblUsers(userID)
);
