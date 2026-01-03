import { useState, useEffect } from "react";
import { useEditor, EditorContent } from "@tiptap/react";
import StarterKit from "@tiptap/starter-kit";
import styles from "./Editor.module.css";

const MenuBar = ({ editor }) => {
  if (!editor) return null;

  return (
    <div className={styles.menuBar}>
      <button
        onClick={() => editor.chain().focus().toggleBold().run()}
        className={editor.isActive("bold") ? styles.isActive : ""}
      >
        {" "}
        Bold{" "}
      </button>
      <button
        onClick={() => editor.chain().focus().toggleItalic().run()}
        className={editor.isActive("italic") ? styles.isActive : ""}
      >
        {" "}
        Italic{" "}
      </button>
      <button
        onClick={() => editor.chain().focus().toggleBulletList().run()}
        className={editor.isActive("bulletList") ? styles.isActive : ""}
      >
        {" "}
        List{" "}
      </button>
    </div>
  );
};

export default function Editor({ onSave, initialData }) {
  const [title, setTitle] = useState("");

  const editor = useEditor({
    extensions: [StarterKit],
    content: "<p>Start writing here...</p>",
  });

  useEffect(() => {
    if (initialData && editor) {
      setTitle(initialData.title);
      if (initialData.pages && initialData.pages.length > 0) {
        editor.commands.setContent(initialData.pages[0].content);
      }
    } else if (!initialData && editor) {
      setTitle("");
      editor.commands.setContent("<p>Start writing here...</p>");
    }
  }, [initialData, editor]);

  const handleSave = () => {
    if (!title.trim()) {
      alert("Please enter a title!");
      return;
    }
    const content = editor?.getHTML() || "";
    onSave({ title, content });
  };

  return (
    <div className={styles.editorContainer}>
      <input
        className={styles.titleInput}
        type="text"
        placeholder="Entry title..."
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      />

      <MenuBar editor={editor} />

      <div className={styles.editorWrapper}>
        <EditorContent editor={editor} />
      </div>

      <button className={styles.saveButton} onClick={handleSave}>
        Save Entry
      </button>
    </div>
  );
}
