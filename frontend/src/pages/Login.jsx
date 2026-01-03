import { useState, useContext } from "react";
import authService from "../services/authService";
import { AuthContext } from "../context/AuthContext";
import Register from "./Register";
import styles from "./Login.module.css";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showRegister, setShowRegister] = useState(false);
  const { setUser } = useContext(AuthContext);

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const data = await authService.login(username, password);
      setUser(data);
      alert("Login successful!");
    } catch (error) {
      alert(
        "Login error: " +
          (error.response?.data?.message || "Server unreachable")
      );
    }
  };

  if (showRegister) {
    return <Register onSwitchToLogin={() => setShowRegister(false)} />;
  }

  return (
    <div className={styles.loginContainer}>
      <h2 className={styles.title}>Login</h2>
      <form className={styles.form} onSubmit={handleLogin}>
        <input
          className={styles.inputField}
          type="text"
          placeholder="Email"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          className={styles.inputField}
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit" className={styles.submitButton}>
          Sign In
        </button>
      </form>
      <p className={styles.switchText}>
        Don't have an account?{" "}
        <span
          className={styles.switchLink}
          onClick={() => setShowRegister(true)}
        >
          Create one
        </span>
      </p>
    </div>
  );
}
