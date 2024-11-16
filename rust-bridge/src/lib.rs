uniffi::setup_scaffolding!();

#[uniffi::export]
pub fn expensive_calculation(intensity: u64) -> u64 {
    println!("Calculating slowly...");
    // Perform some CPU-intensive calculations
    let mut result = 0;
    for i in 1..=intensity {
        result += fibonacci(i);
    }

    // Simulate some complex business logic
    match intensity {
        1..=3 => result + 10,
        4..=7 => result + 50,
        _ => result + 100,
    }
}

fn fibonacci(n: u64) -> u64 {
    match n {
        0 => 0,
        1 => 1,
        _ => fibonacci(n - 1) + fibonacci(n - 2),
    }
}