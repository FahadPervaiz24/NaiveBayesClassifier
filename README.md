# NaiveBayesClassifier
# Naive Bayes Classifier (Java)

A discrete Naive Bayes classifier built from scratch in Java for predicting nursery school admission recommendations based on categorical features. The model is trained on 10,000 labeled samples and evaluates performance using Maximum A Posteriori (MAP) inference.

---

## 📊 Dataset

The classifier is trained and evaluated on two files:

- `train_data.txt`: Contains 10,000 labeled training samples
- `val_data.txt`: Contains unseen validation samples for prediction accuracy

Each record includes 8 categorical attributes and a class label: `recommend` or `not_recom`.

---

## 🧠 Features

- Computes class priors $P(y)$ and conditional probabilities $P(x_i \mid y)$ for each categorical feature
- Manually constructs Conditional Probability Tables (CPTs)
- Applies Naive Bayes inference using MAP estimation
- Evaluates model accuracy on validation data
- No external ML libraries used — 100% custom implementation

---

## 🛠️ Tech Stack

- Java 17
- Standard Java I/O and `Scanner`
- Plain text dataset parsing

---

## 📈 How It Works

1. Compute prior probabilities from `train_data.txt`
2. Compute conditional likelihoods $P(x_i \mid y)$ for all features
3. Predict labels for validation samples by computing:

$$
\arg\max_{y \in \{recommend, not\_recom\}} \left[ P(y) \cdot \prod_{i=1}^{8} P(x_i \mid y) \right]
$$

4. Output prediction accuracy

---

## 🚀 Running the Code

### 🧾 Requirements

- Java 17+
- `train_data.txt` and `val_data.txt` in the correct file paths (or adjust in `main()`)

### ▶️ Compile & Run


```bash
javac P3.java
java P3
```
You should see output like:
"The accuracy of the current predictions is 99.65%"




