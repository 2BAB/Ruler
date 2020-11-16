rootProject.name = "ruler"
// Test project
include(":demo")

// CPU, Network, Storage, Battery Base, Memory Base (PSS), Heap Dump, Stack Trace
include(":ruler-sampler")

// WakeLock,
include(":ruler-battery-gradle")
include(":ruler-battery-runtime")


//include(":ruler")
//include(":ruler-fluency") // FPS, Main Thread Block, ANR