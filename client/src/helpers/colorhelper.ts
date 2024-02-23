export function generateRandomColor(alpha = "1") {
    return `rgba(${Math.floor(Math.random() * 256)}, 
                                ${Math.floor(Math.random() * 256)}, 
                                ${Math.floor(Math.random() * 256)}, ${alpha}`;
}