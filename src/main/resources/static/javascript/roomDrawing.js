// Get the canvas element and its 2D context
const canvas = document.getElementById('roomIllustration');
const ctx = canvas.getContext('2d');

// Define the dimensions of the 3D rectangle
let width = (room.width * 50) * 2.5;
let height = (room.height * 50) * 1.5;
let depth = room.length * 50;

if (width === 1 && height === 1 && depth === 1){
    width = 400;
    height = 400;
    depth = 400;
}


// Function to draw a 3D rectangle
function draw3DRectangle() {
    // Clear the canvas
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.font = "14px Arial";

    // Draw the front face of the rectangle
    ctx.beginPath();
    ctx.moveTo(0, canvas.height);
    ctx.lineTo(width, canvas.height); // Line between bottom left and bottom right corners of front wall
    ctx.lineTo(width, canvas.height - height); // Line between bottom right and top right corners of front wall
    ctx.lineTo(0, canvas.height - height); // Line between top right and top left corners of front wall
    ctx.lineTo(0, canvas.height); // Line between top left and bottom left corners of front wall

    ctx.fillText(`Width: ${room.width.toFixed(2)} m`, width / 2.5, canvas.height - 5);
    ctx.fillText(`Height: ${room.height.toFixed(2)} m`, width + 5, canvas.height - height / 1.5);
    ctx.fillText(`Length: ${room.length.toFixed(2)} m`, width + 5 + depth / 2, canvas.height - depth / 2.25);

    ctx.lineTo(depth, canvas.height - depth); // Line between bottom left and bottom right of left side wall
    ctx.lineTo(depth, canvas.height - height - depth); // Line between bottom right and top right of left side wall
    ctx.lineTo(0, canvas.height - height); // Line between top right and top left of left side wall

    ctx.moveTo(width, canvas.height);
    ctx.lineTo(width + depth, canvas.height - depth); // Line between bottom left and bottom right of right side wall
    ctx.lineTo(depth, canvas.height - depth); // Line between bottom right and top right of floor
    ctx.moveTo(width, canvas.height - height);
    ctx.lineTo(width + depth, canvas.height - height - depth); // Line between top left and top right of right side wall
    ctx.lineTo(width + depth, canvas.height - depth); // Line between top and bottom right of right side wall
    ctx.moveTo(width + depth, canvas.height - height - depth);
    ctx.lineTo(depth, canvas.height - height - depth); // Line between top right and top left of back wall
    ctx.stroke();
}

// Call the function to draw the 3D rectangle
draw3DRectangle();