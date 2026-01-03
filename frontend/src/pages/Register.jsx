import { useState } from "react";
import authService from "../services/authService";
import styles from "./Login.module.css";

export default function Register({ onSwitchToLogin }) {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleRegister = async (e) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      alert("Passwords don't match!");
      return;
    }

    if (password.length < 6) {
      alert("Password must be at least 6 characters long!");
      return;
    }

    try {
      await authService.register(username, email, password);
      alert("Registration successful! You can now login.");
      onSwitchToLogin();
    } catch (error) {
      alert(
        "Registration error: " +
          (error.response?.data?.message || "Server unreachable")
      );
    }
  };

  return (
    <div className={styles.loginContainer}>
      <h2 className={styles.title}>Create Account</h2>
      <form className={styles.form} onSubmit={handleRegister}>
        <input
          className={styles.inputField}
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          className={styles.inputField}
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          className={styles.inputField}
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <input
          className={styles.inputField}
          type="password"
          placeholder="Confirm Password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          required
        />
        <button type="submit" className={styles.submitButton}>
          Create Account
        </button>
      </form>
      <p className={styles.switchText}>
        Already have an account?{" "}
        <span className={styles.switchLink} onClick={onSwitchToLogin}>
          Sign In
        </span>
      </p>
    </div>
  );
}
