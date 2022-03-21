function PassGenerator() {
    let password = "";
    let charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    for (i=0; i<16; i++) {
        password += charSet.charAt(
            Math.floor(Math.random() * 62)
        );
    }
    return password;
}

export default PassGenerator();