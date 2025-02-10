// Import necessary modules
import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';
import * as CANNON from 'cannon-es';

// URLs for assets
const monkeyUrl = new URL('../assets/doggo2.glb', import.meta.url);
const mapUrl = new URL('../assets/uiuMap.glb', import.meta.url);

// THREE.js Renderer Setup
const renderer = new THREE.WebGLRenderer();
renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);

// Scene and Camera Setup
const scene = new THREE.Scene();
const camera = new THREE.PerspectiveCamera(
    45,
    window.innerWidth / window.innerHeight,
    0.1,
    1000
);
renderer.setClearColor(0xA3A3A3);

// Set up the OrbitControls (but we will disable it for a first-person view)
const orbit = new OrbitControls(camera, renderer.domElement);
camera.position.set(0, 1, 0); // Camera at the cube's position initially
orbit.enableZoom = false; // Disable zoom for first-person view
orbit.enableRotate = false; // Disable rotation for first-person view

// Grid Helper
const grid = new THREE.GridHelper(30, 30);
scene.add(grid);

// Ambient and Directional Lights
const ambientLight = new THREE.AmbientLight(0x404040, 2); // Color and intensity
scene.add(ambientLight);

const directionalLight = new THREE.DirectionalLight(0xffffff, 1.5); // Color and intensity
directionalLight.position.set(10, 20, 10); // Position the light
directionalLight.castShadow = true; // Enable shadows if needed
scene.add(directionalLight);
const lightHelper = new THREE.DirectionalLightHelper(directionalLight);
scene.add(lightHelper);

// Cannon.js World Setup
const world = new CANNON.World();
world.gravity.set(0, -9.82, 0); // Gravity

// Add a Physics Plane for the Grid (with gravity)
const planeBody = new CANNON.Body({
    mass: 0, // Static body (gravity won't affect it directly)
    shape: new CANNON.Plane(),
    position: new CANNON.Vec3(0, 0, 0),
});
planeBody.quaternion.setFromEuler(-Math.PI / 2, 0, 0); // Rotate to align with the grid
world.addBody(planeBody);

// GLTF Loader for Map and Animated Object
const assetLoader = new GLTFLoader();
let mixer;
let mapModel;

// Load Animated Object
assetLoader.load(monkeyUrl.href, function (gltf) {
    const model = gltf.scene;
    scene.add(model);
    mixer = new THREE.AnimationMixer(model);
    const clips = gltf.animations;

    // Play all animations
    clips.forEach(function (clip) {
        const action = mixer.clipAction(clip);
        action.play();
    });
}, undefined, function (error) {
    console.error(error);
});

// Load Map (single mesh) and Make It a Solid Body
assetLoader.load(mapUrl.href, function (gltf) {
    mapModel = gltf.scene;
    scene.add(mapModel);

    // Set map position to y = -7.8 and fix it
    mapModel.position.set(0, -7.8, 0); // Fixed position for the map

    // Since it's a single mesh, we get the geometry directly
    const geometry = mapModel.children[0].geometry;
    
    // Create a bounding box around the map geometry
    const boundingBox = new THREE.Box3().setFromObject(mapModel);
    const size = boundingBox.getSize(new THREE.Vector3());

    // Create a ConvexPolyhedron for the map
    const vertices = [];
    const positions = geometry.attributes.position.array;

    for (let i = 0; i < positions.length; i += 3) {
        vertices.push(new CANNON.Vec3(positions[i], positions[i + 1], positions[i + 2]));
    }

    const shape = new CANNON.ConvexPolyhedron(vertices);
    const mapBody = new CANNON.Body({
        mass: 0, // Static body
        position: new CANNON.Vec3(mapModel.position.x, mapModel.position.y, mapModel.position.z),
    });

    mapBody.addShape(shape);
    world.addBody(mapBody);

}, undefined, function (error) {
    console.error(error);
});

// Add a Placeholder Dynamic Object (Cube) with Physics
const placeholderGeometry = new THREE.BoxGeometry(1, 1, 1);
const placeholderMaterial = new THREE.MeshStandardMaterial({ color: 0x00ff00 });
const placeholderMesh = new THREE.Mesh(placeholderGeometry, placeholderMaterial);
scene.add(placeholderMesh);

// Create Cannon Body for Cube (solid body)
const placeholderBody = new CANNON.Body({
    mass: 1, // Dynamic body, can be affected by physics
    position: new CANNON.Vec3(0, 10, 0), // Start above the grid
});

// Create a box shape for the cube
const cubeShape = new CANNON.Box(new CANNON.Vec3(0.5, 0.5, 0.5)); // Half-dimensions for collision
placeholderBody.addShape(cubeShape);
world.addBody(placeholderBody);

// Key Input for Movement
const keys = {
    W: false,
    A: false,
    S: false,
    D: false
};

window.addEventListener('keydown', (event) => {
    if (event.key === 'w' || event.key === 'W') keys.W = true;
    if (event.key === 'a' || event.key === 'A') keys.A = true;
    if (event.key === 's' || event.key === 'S') keys.S = true;
    if (event.key === 'd' || event.key === 'D') keys.D = true;
});

window.addEventListener('keyup', (event) => {
    if (event.key === 'w' || event.key === 'W') keys.W = false;
    if (event.key === 'a' || event.key === 'A') keys.A = false;
    if (event.key === 's' || event.key === 'S') keys.S = false;
    if (event.key === 'd' || event.key === 'D') keys.D = false;
});

// Steering and Movement
let steeringAngle = 0; // Initial steering angle (in radians)
const moveSpeed = 0.1;
const steeringSpeed = 0.05; // How fast the cube turns

// Sync Physics and Rendering
function animate() {
    world.step(1 / 60); // Step the physics world

    // Steering and movement logic
    if (keys.S) { // Move forward
        placeholderBody.position.x += Math.sin(steeringAngle) * moveSpeed;
        placeholderBody.position.z -= Math.cos(steeringAngle) * moveSpeed;
    }
    if (keys.W) { // Move backward
        placeholderBody.position.x -= Math.sin(steeringAngle) * moveSpeed;
        placeholderBody.position.z += Math.cos(steeringAngle) * moveSpeed;
    }

    if (keys.D) { // Turn left
        steeringAngle += steeringSpeed;
    }
    if (keys.A) { // Turn right
        steeringAngle -= steeringSpeed;
    }

    // Sync the cube's position and rotation with physics
    placeholderMesh.position.copy(placeholderBody.position);
    placeholderMesh.quaternion.copy(placeholderBody.quaternion);

    // Apply rotation to the cube for steering effect
    placeholderMesh.rotation.y = steeringAngle;

    // Update the camera's position to follow the cube (first-person view)
    camera.position.copy(placeholderBody.position);
    camera.position.y += 1; // Position the camera slightly above the cube
    camera.rotation.copy(placeholderMesh.rotation); // Align camera's rotation with the cube

    // Sync animations
    if (mixer) {
        mixer.update(clock.getDelta());
    }

    renderer.render(scene, camera);
}

const clock = new THREE.Clock();
renderer.setAnimationLoop(animate);

// Handle Window Resize
window.addEventListener('resize', function () {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
});
