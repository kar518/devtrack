import { useEffect, useState } from "react";
import axios from "axios";
import Chart from "chart.js/auto";
import "./App.css";

function App() {

  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [authForm, setAuthForm] = useState({
    username: "",
    password: ""
  });

  const [problems, setProblems] = useState([]);
  const [stats, setStats] = useState({});
  const [activeTab, setActiveTab] = useState("add");

  const [form, setForm] = useState({
    title: "",
    language: "",
    difficulty: "Easy",
    date: "",
    topic: "",
    timeSpent: 0,
    struggleLevel: "Low"
  });

  // ✅ Fetch data
  const fetchData = () => {
    axios.get("https://devtrack-lk9v.onrender.com/api/analytics", {
      headers: {
        Authorization: localStorage.getItem("token")
      }
    })
      .then(res => {
        setProblems(res.data.problems);
        setStats(res.data);
      });
  };

  // ✅ Login check
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      setIsLoggedIn(true);
      fetchData();
    }
  }, []);

  // ✅ Refetch when switching to analytics
  useEffect(() => {
    if (activeTab === "analytics") {
      fetchData();
    }
  }, [activeTab]);

  // ✅ FIXED CHART (ONLY ONE — CLEAN)
  useEffect(() => {
    if (activeTab !== "analytics") return;
    if (!stats || stats.easy === undefined) return;

    const canvas = document.getElementById("chart");
    if (!canvas) return;

    const ctx = canvas.getContext("2d");

    if (window.myChart) {
      window.myChart.destroy();
    }

    window.myChart = new Chart(ctx, {
      type: "bar",
      data: {
        labels: ["Easy", "Medium", "Hard"],
        datasets: [{
          label: "Problems",
          data: [
            stats.easy || 0,
            stats.medium || 0,
            stats.hard || 0
          ],
          backgroundColor: ["#10B981", "#F59E0B", "#EF4444"]
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });

  }, [stats, activeTab]);

  // 🔐 LOGIN
  const handleLogin = async () => {
    try {
      const res = await axios.post("http://localhost:8080/api/login", authForm);

      if (res.data.startsWith("TOKEN")) {
        localStorage.setItem("token", res.data);
        setIsLoggedIn(true);
        fetchData();
      } else {
        alert("Login failed");
      }
    } catch {
      alert("Error logging in");
    }
  };

  // 🔐 REGISTER
  const handleRegister = async () => {
    try {
      await axios.post("http://localhost:8080/api/register", authForm);
      alert("Registered! Now login.");
    } catch {
      alert("Error registering");
    }
  };

  // ➕ ADD PROBLEM
  const handleSubmit = () => {
    axios.post("http://localhost:8080/api/add", {
      ...form,
      timeSpent: Number(form.timeSpent)
    }, {
      headers: {
        Authorization: localStorage.getItem("token")
      }
    })
      .then(() => {
        fetchData();
        setForm({
          title: "",
          language: "",
          difficulty: "Easy",
          date: "",
          topic: "",
          timeSpent: 0,
          struggleLevel: "Low"
        });
      });
  };

  // 🚪 LOGOUT
  const handleLogout = () => {
    localStorage.removeItem("token");
    setIsLoggedIn(false);
  };

  // 🔐 LOGIN UI
  if (!isLoggedIn) {
    return (
      <div className="auth-container">
        <div className="card" style={{ width: "400px", textAlign: "center" }}>
          <h2>Welcome Back</h2>

          <input placeholder="Username"
            onChange={(e) => setAuthForm({ ...authForm, username: e.target.value })}
          />

          <input type="password" placeholder="Password"
            onChange={(e) => setAuthForm({ ...authForm, password: e.target.value })}
          />

          <div className="auth-buttons">
            <button className="button" onClick={handleLogin}>Login</button>
            <button className="button button-secondary" onClick={handleRegister}>
              Register
            </button>
          </div>
        </div>
      </div>
    );
  }

  // 🧠 MAIN UI
  return (
    <div className="app">

      <div className="sidebar">
        <h2 className="logo">DevTrack</h2>

        <p className={`nav-item ${activeTab === "add" ? "active" : ""}`} onClick={() => setActiveTab("add")}>✦ New Problem</p>
        <p className={`nav-item ${activeTab === "list" ? "active" : ""}`} onClick={() => setActiveTab("list")}>■ Problem List</p>
        <p className={`nav-item ${activeTab === "analytics" ? "active" : ""}`} onClick={() => setActiveTab("analytics")}>◒ Analytics</p>
        <p className={`nav-item ${activeTab === "calendar" ? "active" : ""}`} onClick={() => setActiveTab("calendar")}>▦ Consistency</p>

        <hr />
        <p className="nav-item logout" onClick={handleLogout}>Logout</p>
      </div>

      <div className="main">

        {/* ADD */}
        {activeTab === "add" && (
          <div className="card center-card">
            <h2>Add Problem</h2>

            <input name="title" placeholder="Title" value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} />
            <input name="language" placeholder="Language" value={form.language} onChange={(e) => setForm({ ...form, language: e.target.value })} />

            <select value={form.difficulty} onChange={(e) => setForm({ ...form, difficulty: e.target.value })}>
              <option>Easy</option>
              <option>Medium</option>
              <option>Hard</option>
            </select>

            <input type="date" value={form.date} onChange={(e) => setForm({ ...form, date: e.target.value })} />

            <input placeholder="Topic" value={form.topic} onChange={(e) => setForm({ ...form, topic: e.target.value })} />
            <input type="number" placeholder="Time (mins)" value={form.timeSpent} onChange={(e) => setForm({ ...form, timeSpent: e.target.value })} />

            <select value={form.struggleLevel} onChange={(e) => setForm({ ...form, struggleLevel: e.target.value })}>
              <option>Low</option>
              <option>Medium</option>
              <option>High</option>
            </select>

            <button onClick={handleSubmit} className="button">Add</button>
          </div>
        )}

        {/* LIST */}
        {activeTab === "list" && (
          <div className="card center-card">
            <h2>Problems</h2>
            <ul>
              {problems.map(p => (
                <li key={p.id}>
                  <strong>{p.title}</strong> — {p.topic} — {p.timeSpent} min
                </li>
              ))}
            </ul>
          </div>
        )}

        {/* ANALYTICS */}
        {activeTab === "analytics" && (
          <div className="dashboard-grid">
            <div className="card">
              <h2>Performance Chart</h2>
              <div style={{ position: "relative", height: "350px", width: "100%" }}>
                <canvas id="chart"></canvas>
              </div>
            </div>

            <div className="card">
              <h2>AI Insights</h2>
              <div className="insights-content">
                {stats.analysis ? stats.analysis : "No insights available yet. Add some problems!"}
              </div>
            </div>
          </div>
        )}

        {/* CALENDAR */}
        {activeTab === "calendar" && (
          <div className="card center-card">
            <h2>Consistency</h2>
            <p>🔥 {stats.streak} day streak</p>

            <div id="calendar" style={{
              display: "grid",
              gridTemplateColumns: "repeat(7, 40px)",
              gap: "6px"
            }}></div>
          </div>
        )}

      </div>
    </div>
  );
}

export default App;
