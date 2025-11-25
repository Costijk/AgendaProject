import Style from "./ToJournalButton.module.css";

export default function ToJournalButton() {
  return (
    <div>
      <button className={Style.Button}>
        <Link to="/journal">Go to Journal</Link>
      </button>
    </div>
  );
}
