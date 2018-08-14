# logarithmic-backoff-sdk
Java-based SDK to add logarithmic backoff functionality to any java function call. Define expected exceptions to automatically trap and retry while waiting a configurable amount of time between successive retries. Any exceptions not defined beforehand are escalated to the caller to interrupt the retry loop.
