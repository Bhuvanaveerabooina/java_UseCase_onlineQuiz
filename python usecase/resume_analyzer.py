import argparse
import os
import re
import string
from collections import Counter

try:
    import nltk
    from nltk.corpus import stopwords
    from nltk.stem import WordNetLemmatizer
    from nltk.tokenize import word_tokenize
except ImportError:
    raise ImportError("Please install required packages with 'pip install -r requirements.txt'.")

try:
    import pdfplumber
except ImportError:
    pdfplumber = None

SKILL_KEYWORDS = {
    "python",
    "sql",
    "java",
    "c++",
    "c",
    "javascript",
    "html",
    "css",
    "excel",
    "power bi",
    "tableau",
    "data analysis",
    "data analytics",
    "data visualization",
    "machine learning",
    "deep learning",
    "nlp",
    "natural language processing",
    "statistics",
    "communication",
    "teamwork",
    "project management",
    "problem solving",
    "research",
    "git",
    "linux",
    "cloud",
    "aws",
    "azure",
    "docker",
    "kubernetes",
    "dsa",
    "analysis",
    "reporting",
    "presentation",
    "leadership",
    "sql server",
    "mysql",
    "postgresql",
    "r",
    "matlab",
    "tensorflow",
    "pytorch",
    "scikit-learn",
    "pandas",
    "numpy",
    "data mining",
    "statistics",
}


def ensure_nltk_data():
    nltk_data = ["punkt", "punkt_tab", "stopwords", "wordnet", "omw-1.4"]
    for package in nltk_data:
        try:
            if package in {"punkt", "punkt_tab"}:
                nltk.data.find(f"tokenizers/{package}")
            else:
                nltk.data.find(package)
        except LookupError:
            nltk.download(package)


def read_text_file(path: str) -> str:
    with open(path, "r", encoding="utf-8", errors="ignore") as file:
        return file.read()


def read_pdf_file(path: str) -> str:
    if pdfplumber is None:
        raise RuntimeError("pdfplumber is not installed. Install dependencies from requirements.txt.")

    text = []
    with pdfplumber.open(path) as pdf:
        for page in pdf.pages:
            page_text = page.extract_text()
            if page_text:
                text.append(page_text)
    return "\n".join(text)


def load_document(path: str) -> str:
    if not os.path.exists(path):
        raise FileNotFoundError(f"File not found: {path}")

    extension = os.path.splitext(path)[1].lower()
    if extension == ".pdf":
        return read_pdf_file(path)
    elif extension in {".txt", ".md"}:
        return read_text_file(path)
    else:
        raise ValueError("Unsupported file type. Use .pdf, .txt, or .md file.")


def normalize_text(text: str) -> str:
    text = text.lower()
    text = text.replace("-", " ")
    text = re.sub(r"[^a-z0-9\s]", " ", text)
    text = re.sub(r"\s+", " ", text)
    return text.strip()


def extract_keywords(text: str, top_n: int = 30) -> list[str]:
    ensure_nltk_data()
    normalized = normalize_text(text)
    try:
        tokens = word_tokenize(normalized)
    except LookupError:
        tokens = re.findall(r"\b[a-z]+\b", normalized)
    words = [w for w in tokens if w not in stopwords.words("english") and w not in string.punctuation and w.isalpha()]
    lemmatizer = WordNetLemmatizer()
    lemmas = [lemmatizer.lemmatize(word) for word in words]
    counts = Counter(lemmas)
    return [word for word, _ in counts.most_common(top_n)]


def find_skills(text: str) -> set[str]:
    normalized = normalize_text(text)
    found = set()
    for skill in SKILL_KEYWORDS:
        pattern = r"\b" + re.escape(skill.lower()) + r"\b"
        if re.search(pattern, normalized):
            found.add(skill)
    return found


def compute_match_score(resume_skills: set[str], job_skills: set[str]) -> float:
    if not job_skills:
        return 0.0
    matched = resume_skills.intersection(job_skills)
    return 100.0 * len(matched) / len(job_skills)


def suggest_missing_skills(resume_skills: set[str], job_skills: set[str]) -> list[str]:
    return sorted(job_skills.difference(resume_skills))


def print_report(resume_path: str, job_path: str):
    resume_text = load_document(resume_path)
    job_text = load_document(job_path)

    resume_keywords = extract_keywords(resume_text)
    resume_skills = find_skills(resume_text)
    job_skills = find_skills(job_text)
    score = compute_match_score(resume_skills, job_skills)
    missing = suggest_missing_skills(resume_skills, job_skills)

    print("\n=== Resume Analyzer Report ===\n")
    print(f"Resume file: {resume_path}")
    print(f"Job file: {job_path}\n")

    print("Detected resume skills:")
    print(", ".join(sorted(resume_skills)) if resume_skills else "No skills detected.")
    print("\nTop extracted resume keywords:")
    print(", ".join(resume_keywords[:20]) if resume_keywords else "No keywords extracted.")

    print("\nDetected job requirement skills:")
    print(", ".join(sorted(job_skills)) if job_skills else "No skills detected in job description.")

    print(f"\nMatch score: {score:.1f}%")
    print("\nSuggested missing skills:")
    print(", ".join(missing) if missing else "Your resume already covers the detected job skill keywords.")
    print("\n==============================\n")


def print_report_from_text(resume_text: str, job_text: str, resume_source: str = "text input", job_source: str = "text input"):
    resume_keywords = extract_keywords(resume_text)
    resume_skills = find_skills(resume_text)
    job_skills = find_skills(job_text)
    score = compute_match_score(resume_skills, job_skills)
    missing = suggest_missing_skills(resume_skills, job_skills)

    print("\n=== Resume Analyzer Report ===\n")
    print(f"Resume source: {resume_source}")
    print(f"Job source: {job_source}\n")

    print("Detected resume skills:")
    print(", ".join(sorted(resume_skills)) if resume_skills else "No skills detected.")
    print("\nTop extracted resume keywords:")
    print(", ".join(resume_keywords[:20]) if resume_keywords else "No keywords extracted.")

    print("\nDetected job requirement skills:")
    print(", ".join(sorted(job_skills)) if job_skills else "No skills detected in job description.")

    print(f"\nMatch score: {score:.1f}%")
    print("\nSuggested missing skills:")
    print(", ".join(missing) if missing else "Your resume already covers the detected job skill keywords.")
    print("\n==============================\n")


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Resume Analyzer & Job Matcher")
    parser.add_argument(
        "--resume",
        help="Path to the resume file (.txt or .pdf)",
        default=None,
    )
    parser.add_argument(
        "--job",
        help="Path to the job description file (.txt or .pdf)",
        default=None,
    )
    parser.add_argument(
        "--resume-text",
        help="Resume content as text (alternative to --resume file)",
        default=None,
    )
    parser.add_argument(
        "--job-text",
        help="Job description content as text (alternative to --job file)",
        default=None,
    )
    return parser.parse_args()


def get_interactive_input():
    print("Choose resume input method:")
    print("1. Enter text directly")
    print("2. Provide file path (supports .txt, .pdf, .md)")
    resume_choice = input("Enter 1 or 2: ").strip()

    if resume_choice == "2":
        resume_path = input("Enter resume file path: ").strip()
        try:
            resume_text = load_document(resume_path)
            print(f"Loaded resume from: {resume_path}")
        except Exception as e:
            print(f"Error loading resume file: {e}")
            print("Falling back to sample resume.")
            resume_text = load_document("sample_resume.txt")
    else:
        print("Enter your resume content (press Enter twice on empty lines to finish):")
        resume_lines = []
        empty_count = 0
        while empty_count < 2:
            try:
                line = input()
                if line == "":
                    empty_count += 1
                else:
                    empty_count = 0
                    resume_lines.append(line)
            except EOFError:
                break
        resume_text = "\n".join(resume_lines)

        if not resume_text.strip():
            print("No resume text entered. Using sample resume.")
            resume_text = load_document("sample_resume.txt")

    print("\nChoose job description input method:")
    print("1. Enter text directly")
    print("2. Provide file path (supports .txt, .pdf, .md)")
    job_choice = input("Enter 1 or 2: ").strip()

    if job_choice == "2":
        job_path = input("Enter job description file path: ").strip()
        try:
            job_text = load_document(job_path)
            print(f"Loaded job description from: {job_path}")
        except Exception as e:
            print(f"Error loading job file: {e}")
            print("Falling back to sample job description.")
            job_text = load_document("sample_job_description.txt")
    else:
        print("Enter the job description (press Enter twice on empty lines to finish):")
        job_lines = []
        empty_count = 0
        while empty_count < 2:
            try:
                line = input()
                if line == "":
                    empty_count += 1
                else:
                    empty_count = 0
                    job_lines.append(line)
            except EOFError:
                break
        job_text = "\n".join(job_lines)

        if not job_text.strip():
            print("No job text entered. Using sample job description.")
            job_text = load_document("sample_job_description.txt")

    return resume_text, job_text


def main():
    args = parse_args()

    # Determine resume source
    if args.resume_text:
        resume_text = args.resume_text
        resume_source = "command line text"
    elif args.resume:
        resume_text = load_document(args.resume)
        resume_source = args.resume
    else:
        resume_text, job_text = get_interactive_input()
        print_report_from_text(resume_text, job_text, "interactive input", "interactive input")
        return

    # Determine job source
    if args.job_text:
        job_text = args.job_text
        job_source = "command line text"
    elif args.job:
        job_text = load_document(args.job)
        job_source = args.job
    else:
        # If we have resume from file but no job, use sample job
        if args.resume and not args.job_text:
            job_text = load_document("sample_job_description.txt")
            job_source = "sample_job_description.txt"
        else:
            print("Please provide job description via --job file or --job-text.")
            return

    try:
        print_report_from_text(resume_text, job_text, resume_source, job_source)
    except Exception as exc:
        print(f"Error: {exc}")


if __name__ == "__main__":
    main()
