# Descriptive-Modeling-and-Clustering-of-Textual-Data

## Part 1: Descriptive modeling of textual data

### Dataset
The dataset consists of three document folders, each containing eight different documents. The documents are relevant global news articles, all in English and of varying lengths.

### Goal
Each document folder contains documents from a different domain. The goal of this first part
of the assignment will be to auto-generate keywords, or topics, for each document folder. This task is
also known as features extraction.

### Preprocessing

The first step is to preprocess each document in the dataset using Java Stanford CoreNLP library. The simple API is sufficient to perform some of the
tasks for the purpose of this assignment by following steps:

1. Filter and remove stop words. Stop words are words such as “the”, “of”, “and”, etc. and
usually do not contain any meaningful information for identifying document topics or similarities. The CoreNLP library contains a good list of stop words, but there are many others available online that you can use. Google is known to have an up to date stop words and it is
available on the web, you may have to use Google’s stop words for better results.

2. Apply tokenization, stemming and lemmatization. Tokens are the words taken from a
block of text once it has been split into its individual words (called tokens). It is common to
also remove punctuation at the same time. Lemmatization refers to regularizing the resulting
list of words based on their part-of-speech (POS) tags.

3. Apply named-entity extraction (NER). NER aims to overcome a common problem in
separating words by only using whitespace characters between the words. For example, “the
Microsoft Corporation” has three tokens. “The” is a stop word and should be removed, and
“Microsoft Corporation” should really be treated as one token. By using NER we can identify a set or a group of words that have a single meaning and combine them to a single token. This technique most commonly applies to the names of people or organizations.

4. Use a sliding window approach to merge remaining phrases that belong together.
While NER usually relies on built-in word lists or capitalization of entity tokens, there are
other words that consist of one or more word forms (called compounds). For example,
“computer science”, “beauty pageant”, or “student athlete compensation” are all phrases
that frequently occur together and have a single meaning.

### Generating a document term matrix
After preprocessing, construct the document-term matrix. Each row in the matrix
should correspond to one document in the input dataset. Each column should represent one
term/key-phrase/key word (concept) of the final set of terms across all documents (after tokenization, lemmatization, and merging NER and n-gram tokens). Each cell should contain the number of times that each term occurred in the each document. This will result in a relatively sparse vector representation of each document, since only a few of the complete list of terms will occur in each document and most of the values in the matrix will be zero.

Once your matrix is constructed, you should transform it using term frequency–inverse document
frequency (TF-IDF). TF-IDF is a very useful measure in text mining that helps with down-weighting
terms that are frequent across all documents while promoting terms that occur frequently in the current document, but are generally rare. Finally, use the transformed matrix to generate keywords, or topics, for each document
folder, for example by combining all the document vectors together and then sorting the terms for
each folder based on their TF-IDF scores.



## Part 2: Clustering textual data

After converting unstructured collections of documents to a document-term matrix,
develop a clustering algorithm that should group similar documents vectors back into their original
folder structure.

### K-means clustering and document similarity
Implement k-means clustering algorithm that takes as
input the TF-IDF matrix generated in part 1, and an integer k that specifies the number of output clusters. Since there are 3 document folders in the dataset, setting k=3 is likely to result in best results.

#### Why cosine similarity over Euclidean distance?
The major difference between Euclidean distance and cosine similarity is that the former is purely a
distance measure, whereas the latter measures the angle between vectors without taking their magnitude into account. In other words, by using cosine similarity instead of Euclidean distance we are
able to remove the effect of mere word count/frequency, which is usually desirable when dealing
with documents of different lengths (since two documents of unequal length might still be about the
same topic and thus semantically similar). Mathematically, computing the cosine similarity is equivalent to computing the Euclidean distance between normalized unit vectors. Please note that you are
not allowed to use any existing Java libraries for this step.
both, and you should compare your results for both measures.
