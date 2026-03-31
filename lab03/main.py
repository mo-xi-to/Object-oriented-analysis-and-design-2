import tkinter as tk
from tkinter import ttk, messagebox, filedialog

from exporters import CSVExporter, HTMLExporter

class App:
    def __init__(self, root):
        self.root = root
        self.root.title("Data Reporter Pro")
        self.root.geometry("850x600")

        self.headers = ["ID", "Товар", "Кол-во", "Цена"]
        self.rows = []

        top_frame = tk.Frame(root, pady=10)
        top_frame.pack(fill="x", padx=15)

        tk.Label(top_frame, text="Колонка:").pack(side="left")
        self.col_entry = tk.Entry(top_frame, width=15)
        self.col_entry.pack(side="left", padx=5)
        tk.Button(top_frame, text="Добавить колонку", command=self.add_column).pack(side="left")
        
        tk.Button(top_frame, text="Загрузить базовый шаблон", command=self.load_base_template, bg="#eee").pack(side="right")

        mid_frame = tk.Frame(root)
        mid_frame.pack(fill="both", expand=True, padx=15)

        self.tree = ttk.Treeview(mid_frame, show="headings")
        self.tree.pack(side="left", fill="both", expand=True)
        self._refresh_tree_headers()

        input_frame = tk.LabelFrame(root, text=" Добавить строку данных (через запятую) ", padx=10, pady=10)
        input_frame.pack(fill="x", padx=15, pady=10)

        self.row_entry = tk.Entry(input_frame, font=("Arial", 11))
        self.row_entry.pack(side="left", fill="x", expand=True, padx=5)
        
        tk.Button(input_frame, text="Добавить", command=self.add_row, width=15, bg="#e3f2fd").pack(side="left", padx=5)
        tk.Button(input_frame, text="Удалить", command=self.delete_row).pack(side="left")

        bottom_frame = tk.Frame(root, pady=10)
        bottom_frame.pack(fill="x", padx=15)

        self.format_var = tk.StringVar(value="CSV")
        tk.Radiobutton(bottom_frame, text="Excel (CSV)", variable=self.format_var, value="CSV").pack(side="left")
        tk.Radiobutton(bottom_frame, text="Web (HTML)", variable=self.format_var, value="HTML").pack(side="left", padx=10)
        
        tk.Button(bottom_frame, text="СОХРАНИТЬ ОТЧЕТ", command=self.save_file, 
                  bg="#2e7d32", fg="white", font=("Arial", 10, "bold"), padx=20).pack(side="right")

    def _refresh_tree_headers(self):
        self.tree["columns"] = self.headers
        for h in self.headers:
            self.tree.heading(h, text=h)
            self.tree.column(h, width=120, anchor="center")

    def add_column(self):
        new_col = self.col_entry.get().strip()
        if new_col:
            self.headers.append(new_col)
            self._refresh_tree_headers()
            self.col_entry.delete(0, tk.END)

    def add_row(self):
        vals = [v.strip() for v in self.row_entry.get().split(",")]
        if len(vals) != len(self.headers):
            messagebox.showwarning("Ошибка", f"Нужно ввести {len(self.headers)} значений через запятую")
            return
        self.rows.append(vals)
        self.tree.insert("", tk.END, values=vals)

    def delete_row(self):
        for s in self.tree.selection():
            idx = self.tree.index(s)
            self.tree.delete(s)
            self.rows.pop(idx)

    def load_base_template(self):
        self.headers = ["ID", "Товар", "Кол-во", "Цена"]
        self.rows = [
            ["1", "MacBook Air", "1", "120000"],
            ["2", "iPhone 15", "2", "90000"],
            ["3", "AirPods", "3", "25000"]
        ]
        self._refresh_tree_headers()
        self.tree.delete(*self.tree.get_children())
        for r in self.rows: self.tree.insert("", tk.END, values=r)

    def save_file(self):
        if not self.rows: return
        fmt = self.format_var.get()
        ext = ".csv" if fmt == "CSV" else ".html"
        
        exporter = CSVExporter() if fmt == "CSV" else HTMLExporter()
        
        path = filedialog.asksaveasfilename(defaultextension=ext, filetypes=[(fmt, f"*{ext}")])
        if path:
            if exporter.export(self.headers, self.rows, path):
                messagebox.showinfo("Успех", f"Отчет сохранен!\nПуть: {path}")

if __name__ == "__main__":
    root = tk.Tk()
    app = App(root)
    root.mainloop()