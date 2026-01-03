import { useState, useEffect } from "react";
import entryService from "../services/entryService";
import styles from "./Sidebar.module.css";

const formatDate = (dateString) => {
  if (!dateString) return "";
  const date = new Date(dateString);
  return date.toLocaleString("en-US", {
    month: "short",
    day: "numeric",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

export default function Sidebar({ onSelectNote, refreshTrigger }) {
  const [notes, setNotes] = useState([]);

  useEffect(() => {
    const fetchNotes = async () => {
      try {
        const response = await entryService.getAllEntries(0, 20);
        setNotes(response.content || []);
      } catch (error) {
        console.error("Error loading entries:", error);
      }
    };
    fetchNotes();
  }, [refreshTrigger]);

  return (
    <div className={styles.sidebar}>
      <h3 className={styles.title}>My Entries</h3>
      <ul className={styles.noteList}>
        {notes.map((note) => (
          <li
            key={note.id}
            className={styles.noteItem}
            onClick={() => onSelectNote(note.id)}
          >
            <span className={styles.noteTitle}>{note.title}</span>
            <div className={styles.noteDates}>
              <span className={styles.noteDate}>
                Created: {formatDate(note.createdAt)}
              </span>
              {note.updatedAt && (
                <span className={styles.noteDate}>
                  Updated: {formatDate(note.updatedAt)}
                </span>
              )}
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
