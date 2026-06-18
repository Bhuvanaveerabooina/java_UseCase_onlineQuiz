# Resume Analyzer & Job Matcher

A Python tool for fresh graduates to analyze resumes and match them with job descriptions.

## Features

- Upload/read resume text or PDF files
- Extract keywords using NLP with `nltk`
- Detect resume skills and compare them with job requirements
- Generate a match score and suggest missing skills
- Support for direct text input, file uploads, and interactive mode
- Help users tailor resumes for better job alignment

## Installation

1. Create a Python virtual environment.

```bash
python -m venv venv
venv\\Scripts\\activate
```

2. Install dependencies.

```bash
pip install -r requirements.txt
```

## Usage

### Using files (recommended for longer content):
```bash
python resume_analyzer.py --resume sample_resume.txt --job sample_job_description.txt
```

### Using direct text input:
```bash
python resume_analyzer.py --resume-text "I have experience with Python, SQL, and machine learning..." --job sample_job_description.txt
```

### Interactive mode (no arguments):
```bash
python resume_analyzer.py
```
This will prompt you to choose between:
- Entering text directly (paste your resume/job content)
- Providing a file path (supports .txt, .pdf, .md files - including PDF resumes!)

Press Enter twice on empty lines to finish text input. If you enter nothing, it will fall back to the sample files.

### Mixed input:
```bash
python resume_analyzer.py --resume sample_resume.txt --job-text "Looking for Python developer with SQL skills..."
```

### Command line options:
- `--resume FILE`: Path to resume file (.txt or .pdf)
- `--job FILE`: Path to job description file (.txt or .pdf)
- `--resume-text TEXT`: Resume content as direct text input
- `--job-text TEXT`: Job description as direct text input

If you omit arguments, the script will use the default sample files:

```bash
python resume_analyzer.py
```

This uses `sample_resume.txt` and `sample_job_description.txt` by default.

## Sample files

- `sample_resume.txt` — a sample resume text.
- `sample_job_description.txt` — a sample job description.

## Output

The script will print:

- extracted resume keywords
- detected resume skills
- job requirements skills
- match score
- missing skills suggestions

## Notes

If this is the first time running, the script will download required NLTK data (`stopwords`, `punkt`, `wordnet`).