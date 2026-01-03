import { useState, useContext } from "react";
import { AuthContext } from "./context/AuthContext";
import Login from "./pages/Login";
import Editor from "./components/Editor";
import Sidebar from "./components/Sidebar";
import entryService from "./services/entryService";
import authService from "./services/authService";
import styles from "./App.module.css";

export default function App() {
  const { user, setUser } = useContext(AuthContext);
  const [selectedNote, setSelectedNote] = useState(null);
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  const handleNoteSelect = async (id) => {
    try {
      const data = await entryService.getEntry(id);
      setSelectedNote(data);
    } catch (error) {
      console.error("Error loading entry:", error);
      alert("Error loading entry.");
    }
  };

  const handleSaveNote = async (noteData) => {
    try {
      if (selectedNote?.id) {
        const response = await entryService.updateEntry(
          selectedNote.id,
          noteData.title,
          noteData.content
        );
        setSelectedNote(response.data);
        setRefreshTrigger((prev) => prev + 1);
        alert("Entry updated successfully!");
      } else {
        const response = await entryService.createEntry(
          noteData.title,
          noteData.content
        );
        setSelectedNote(response.data);
        setRefreshTrigger((prev) => prev + 1);
        alert("Entry saved successfully!");
      }
    } catch (error) {
      console.error(
        "Error saving entry:",
        error.response?.data || error.message
      );
      alert("Error saving entry.");
    }
  };

  const handleDeleteNote = async () => {
    if (!selectedNote?.id) {
      alert("No entry selected!");
      return;
    }

    if (confirm("Are you sure you want to delete this entry?")) {
      try {
        await entryService.deleteEntry(selectedNote.id);
        setSelectedNote(null);
        setRefreshTrigger((prev) => prev + 1);
        alert("Entry deleted successfully!");
      } catch (error) {
        console.error("Error deleting entry:", error);
        alert("Error deleting entry.");
      }
    }
  };

  const handleLogout = () => {
    authService.logout();
    setUser(null);
    setSelectedNote(null);
  };

  if (!user) return <Login />;

  return (
    <div className={styles.mainLayout}>
      <Sidebar
        onSelectNote={handleNoteSelect}
        refreshTrigger={refreshTrigger}
      />

      <div className={styles.contentArea}>
        <div className={styles.topBar}>
          <span>{user.email}</span>

          <div className={styles.topBarButtons}>
            <button onClick={() => setSelectedNote(null)}>+ New Entry</button>
            {selectedNote?.id && (
              <button onClick={handleDeleteNote} className={styles.deleteBtn}>
                Delete
              </button>
            )}
            <button onClick={handleLogout} className={styles.logoutBtn}>
              Logout
            </button>
          </div>
        </div>

        <Editor onSave={handleSaveNote} initialData={selectedNote} />
      </div>
    </div>
  );
}
